package com.boun.service.impl;

import static com.google.common.base.Predicates.in;
import static org.simmetrics.builders.StringMetricBuilder.with;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.cache.TagCache;
import com.boun.data.cache.TagCache.TaggedEntityMetaData;
import com.boun.data.common.Constants;
import com.boun.data.dbpedia.OWLClassHierarchy;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.dbpedia.SPARQLRunner;
import com.boun.http.request.BasicSearchRequest;
import com.boun.http.request.TagData;
import com.boun.http.request.TagSearchRequest;
import com.boun.http.response.QueryLabelResponse;
import com.boun.http.response.SemanticSearchResponse;
import com.boun.service.DiscussionService;
import com.boun.service.GroupService;
import com.boun.service.MeetingService;
import com.boun.service.NoteService;
import com.boun.service.PinkElephantService;
import com.boun.service.ResourceService;
import com.boun.service.SemanticTagSearchService;
import com.boun.service.TagService;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import lombok.Data;

@Service
public class SemanticTagSearchServiceImpl extends PinkElephantService implements SemanticTagSearchService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private DiscussionService discussionService;
	
	@Autowired
	private NoteService noteService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private MeetingService meetingService;
	
	@Override
	public QueryLabelResponse querySearchString(BasicSearchRequest request) {
		
		validate(request);
		
		List<TagData> tagDataList = TagCache.getInstance(tagService).getAllTags();
		if(tagDataList == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.TAG_NOT_FOUND, "");
		}
		
		QueryLabelResponse response = new QueryLabelResponse(request.getQueryString());
		
		for (TagData tagData : tagDataList) {
			float result = getSimilarityIndex(tagData.getTag(), request.getQueryString());
			
			if(result == 0){
				continue;
			}
			
			response.addData(tagData.getTag(), tagData.getClazz(), null);
		}
		
		return response;
	}
	
	public QueryLabelResponse queryLabel(BasicSearchRequest request){
		
		validate(request);
		
//		return HTTPQueryRunner.getInstance().runQuery(request.getQueryString());
		
		return SPARQLRunner.getInstance().runQuery(request.getQueryString());
	}
	
	public SemanticSearchResponse searchNew(TagSearchRequest request){
		
		SemanticSearchResponse response = new SemanticSearchResponse();
		
		TagData tagData = request.getTagData();
		if(tagData == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Query string cannot be null", "");
		}
	
		List<SemanticSearchIndex> searchIndex = new ArrayList<SemanticSearchIndex>();
		
		List<TagData> tagList = TagCache.getInstance(tagService).getAllTags();
		for (TagData tag : tagList) {
			
			if(tagData.getClazz() != null && tag.getClazz() != null){
				
				if(tag.equals(tagData)){
					searchIndex.add(new SemanticSearchIndex(tag, Constants.SEMANTIC_SAME_TAG_FACTOR)); //If both these tags are same, mark it with highest value	
				}else if(tag.getClazz().equalsIgnoreCase(tagData.getClazz())){
					
					float similarityIndex = getSimilarityIndex(tag.getTag(), tagData.getTag());
					
					searchIndex.add(new SemanticSearchIndex(tag, Constants.SEMANTIC_SAME_CONTEXT_FACTOR + similarityIndex)); //If both these tags have same class, mark it with higher value
					
				}else{
					
					String clazz1URI = OWLClassHierarchy.getInstance().getClazzURI(tagData.getClazz());
					String clazz2URI = OWLClassHierarchy.getInstance().getClazzURI(tag.getClazz());
					
					float level = getRelationLevel(clazz1URI, clazz2URI); 
					if(level != 0){
						searchIndex.add(new SemanticSearchIndex(tag, level)); //If both these tags have relation, mark it with high value
					}
				}
			}else if(tagData.getClazz() == null || "".equalsIgnoreCase(tagData.getClazz())){
				
				String clazz1URI = OWLClassHierarchy.getInstance().getClazzURI(tagData.getTag());
				if(clazz1URI != null){
					//If search string is an actual class, then process it as if it is a context information
					String clazz2URI = OWLClassHierarchy.getInstance().getClazzURI(tag.getClazz());
					
					float level = getRelationLevel(clazz1URI, clazz2URI);
					if(level != 0){
						searchIndex.add(new SemanticSearchIndex(tag, level)); //If both these tags have relation, mark it with high value
					}
				}else{
					
					//Compare entered text with context information, if they are similar at a degree, consider it as a possible result
					
					float similarityIndex = 0;
					if(tag.getClazz() != null){
						similarityIndex = getSimilarityIndex(tag.getClazz(), tagData.getTag());
						if(similarityIndex > 0.5F){
							searchIndex.add(new SemanticSearchIndex(tag, similarityIndex));
							continue;
						}	
					}
					//If both input tag has no context, compare their similarity 
					similarityIndex = getSimilarityIndex(tag.getTag(), tagData.getTag());
					if(similarityIndex != 0){
						searchIndex.add(new SemanticSearchIndex(tag, similarityIndex));
					}	
				}
			}
		}
		
		if(searchIndex.isEmpty()){
			return response;
		}
		
		Collections.sort(searchIndex, new SemanticSearchIndexSort());
		
		for (SemanticSearchIndex index : searchIndex) {
			List<TaggedEntityMetaData> tagEntityIdList = TagCache.getInstance(tagService).getTag(index.getTag());
			
			if(tagEntityIdList == null || tagEntityIdList.isEmpty()){
				continue;
			}
			
			for (TaggedEntityMetaData taggedEntityMetaData : tagEntityIdList) {
				addResultList(response, resolveEntity(taggedEntityMetaData), index.getTag(), index.getSimilarityIndex());
			}
		}

		return response;
	}
	
	private float getRelationLevel(String clazz1URI, String clazz2URI){
		int level = OWLClassHierarchy.getInstance().isChild(clazz1URI, clazz2URI, 0);
		if(level == 0){
			level = OWLClassHierarchy.getInstance().isChild(clazz2URI, clazz1URI, 0);	
		}
		 
		if(level == 0){
			return 0;
		}
		
		return (Constants.SEMANTIC_CONTEXT_RELATION_FACTOR / level);		
	}
	
	@Override
	public SemanticSearchResponse search(TagSearchRequest request) {

		validate(request);
		
		SemanticSearchResponse response = new SemanticSearchResponse();
		
		TagData tagData = request.getTagData();
		if(tagData == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Query string cannot be null", "");
		}

		List<SemanticSearchIndex> searchIndex = new ArrayList<SemanticSearchIndex>();
		
		List<TagData> tagList = TagCache.getInstance(tagService).getAllTags();
		for (TagData tag : tagList) {
			
			if(tagData.getClazz() == null){
				float similarityIndex = getSimilarityIndex(tag.getTag(), tagData.getTag());
				
				if(similarityIndex == 0){
					continue;
				}
				searchIndex.add(new SemanticSearchIndex(tag, similarityIndex));
				
				resolveTagRelations(tag, searchIndex);
				continue;
			}
			
			if(tag.getClazz() != null){
				//tagData.getClazz() is not null
				if(tag.getClazz().equalsIgnoreCase(tagData.getClazz())){
					
					searchIndex.add(new SemanticSearchIndex(tag, 1));
					
				}else{
					String clazz1URI = OWLClassHierarchy.getInstance().getClazzURI(tagData.getClazz());
					String clazz2URI = OWLClassHierarchy.getInstance().getClazzURI(tag.getClazz());
					
					int level = OWLClassHierarchy.getInstance().isChild(clazz1URI, clazz2URI, 0);
					if(level == 0){
						level = OWLClassHierarchy.getInstance().isChild(clazz2URI, clazz1URI, 0);
					}
					
					if(level != 0){
						searchIndex.add(new SemanticSearchIndex(tag, Constants.SEMANTIC_CONTEXT_RELATION_FACTOR / level));	
					}else{
						continue;
					}
				}
				
				resolveTagRelations(tag, searchIndex);
				
				continue;
			}
			
			//tagData.getClazz() is not null and tag.getClazz() is null //TODO what to do?
		}
		
		Collections.sort(searchIndex, new SemanticSearchIndexSort());
		
		for (SemanticSearchIndex index : searchIndex) {
			List<TaggedEntityMetaData> tagEntityIdList = TagCache.getInstance(tagService).getTag(index.getTag());
			
			if(tagEntityIdList == null || tagEntityIdList.isEmpty()){
				continue;
			}
			
			for (TaggedEntityMetaData taggedEntityMetaData : tagEntityIdList) {
				addResultList(response, resolveEntity(taggedEntityMetaData), index.getTag(), index.getSimilarityIndex());
			}
		}
		
		return response;
	}
	
	private void resolveTagRelations(TagData tag, List<SemanticSearchIndex> searchIndex){
		List<TaggedEntityMetaData> tagEntityIdList = TagCache.getInstance(tagService).getTag(tag);
		if(tagEntityIdList == null || tagEntityIdList.isEmpty()){
			return;
		}
		
		for (TaggedEntityMetaData taggedEntityMetaData : tagEntityIdList) {
			
			TaggedEntity taggedEntity = resolveEntity(taggedEntityMetaData);
			if(taggedEntity ==  null){
				continue;
			}
			
			for (TagData t : taggedEntity.getTagList()) {
				
				if(tag.getClazz() != null && t.getClazz() != null){
					if(!hasRelation(tag, t)){
						continue;
					}
				}
				
				if(tag.getClazz() != null){
					//TODO what if input tag is not null and t.getClass() is null
					continue;
				}
				
				SemanticSearchIndex idx = new SemanticSearchIndex(t, 0); // Set similarity index to zero, because we have found this tag through relations 
				if(searchIndex.contains(idx)){
					continue;
				}
				searchIndex.add(idx);	
				
				resolveTagRelations(t, searchIndex);
			}
		}
	}
	
	private boolean hasRelation(TagData tag, TagData tagData){
		if(tag.getClazz() == null || tagData.getClazz() == null){
			return false;
		}
		
		if(tag.getClazz().equalsIgnoreCase(tagData.getClazz())){
			return true;
		}
		
		String clazz1URI = OWLClassHierarchy.getInstance().getClazzURI(tagData.getClazz());
		String clazz2URI = OWLClassHierarchy.getInstance().getClazzURI(tag.getClazz());
		
		int level = OWLClassHierarchy.getInstance().isChild(clazz1URI, clazz2URI, 0);
		
		if(level != 0){
			return true;
		}
		
		level = OWLClassHierarchy.getInstance().isChild(clazz2URI, clazz1URI, 0);
		if(level != 0){
			return true;
		}
		
		return false;
	}
	
	private TaggedEntity resolveEntity(TaggedEntityMetaData taggedEntityMetaData){
		//TODO cache these entities somehow
		try {
			if (taggedEntityMetaData.getType() == EntityType.DISCUSSION) {
				return discussionService.findById(taggedEntityMetaData.getId());
			}

			if (taggedEntityMetaData.getType() == EntityType.GROUP) {
				return groupService.findById(taggedEntityMetaData.getId());
			}

			if (taggedEntityMetaData.getType() == EntityType.MEETING) {
				return meetingService.findById(taggedEntityMetaData.getId());
			}

			if (taggedEntityMetaData.getType() == EntityType.NOTE) {
				return noteService.findById(taggedEntityMetaData.getId());
			}

			if (taggedEntityMetaData.getType() == EntityType.RESOURCE) {
				return resourceService.findById(taggedEntityMetaData.getId());
			}
		}
		catch (Exception e) {
			//TODO fix
		}
		return null;
	}
	private void addResultList(SemanticSearchResponse response, TaggedEntity taggedEntity, TagData tag, float rank){
		if(taggedEntity == null){
			return;
		}
		
		response.addDetail(taggedEntity.getEntityType(), taggedEntity.getId(), taggedEntity.getDescription(), tag, rank);	
	}
	
	public static float getSimilarityIndex(String str1, String str2){
		
		Set<String> commonWords = Sets.newHashSet("it", "is", "a", "and", "the", "are, i");
		
		StringMetric metric = 
				with(new CosineSimilarity<String>())
				.simplify(Simplifiers.toLowerCase())
				.simplify(Simplifiers.removeNonWord())
				.tokenize(Tokenizers.whitespace())
				.filter(Predicates.not(in(commonWords)))
				.tokenize(Tokenizers.qGram(2))
				.build();
		
		return metric.compare(str1, str2);
	}
	
	@Data
	private static class SemanticSearchIndex{
		private TagData tag;
		private float similarityIndex;
		
		private SemanticSearchIndex(TagData tag, float similarityIndex){
			this.tag = tag;
			this.similarityIndex = similarityIndex;
		}
		
		public boolean equals(Object o){
			if(o == null){
				return false;
			}
			SemanticSearchIndex idx = (SemanticSearchIndex)o;
			
			return (idx.getTag().getTag().equalsIgnoreCase(this.getTag().getTag()));
		}
		
		@Override
		public int hashCode() {
			int code = 7;
			code = 89 * code * this.getTag().getTag().hashCode();
			return code;
		}
	}
	
	private static class SemanticSearchIndexSort implements Comparator<SemanticSearchIndex> {

	    @Override
	    public int compare(SemanticSearchIndex o1, SemanticSearchIndex o2) {
	    	
	    	return (o1.getSimilarityIndex() >= o2.getSimilarityIndex()) ? -1 : 1;
	    }
	}
}