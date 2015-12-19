package com.boun.service.impl;

import static com.google.common.base.Predicates.in;
import static org.simmetrics.builders.StringMetricBuilder.with;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.dbpedia.lookup.ArrayOfResult;
import org.dbpedia.lookup.ArrayOfResult.Result.Classes;
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
import com.boun.data.dbpedia.OWLClassHierarchy;
import com.boun.data.dbpedia.OWLClassHierarchy.Node;
import com.boun.data.http.HTTPClient;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
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

	private static final String DBPEDIA_URL = "http://lookup.dbpedia.org/api/search.asmx/PrefixSearch?MaxHits=20&QueryString=";
	
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
	
	public QueryLabelResponse queryLabel(BasicSearchRequest request){
		
		validate(request);
		
		QueryLabelResponse response = new QueryLabelResponse();
		
		ArrayOfResult arrayOfResult = HTTPClient.getInstance().get(DBPEDIA_URL, request.getQueryString());
		
		OWLClassHierarchy.getInstance().getHierarchy();

		List<ArrayOfResult.Result> resultList = arrayOfResult.getResult();
		for (ArrayOfResult.Result result : resultList) {
			
			Classes classes = result.getClasses();
			List<org.dbpedia.lookup.ArrayOfResult.Result.Classes.Class> classList = classes.getClazz();

			Node current = null;
			for (org.dbpedia.lookup.ArrayOfResult.Result.Classes.Class clazz : classList) {
				
				Node node = OWLClassHierarchy.getInstance().getHierarchy().get(clazz.getURI());
				
				if(node == null){
					continue;
				}
				
				if(current == null){
					current = node;
					continue;
				}
				
				boolean isChild = OWLClassHierarchy.getInstance().isChild(node.getUri(), current.getUri());
				if(isChild){
					current = node;
				}
			}
			if(current == null){
				response.addData(result.getLabel(), null, result.getDescription());	
			}else{
				response.addData(result.getLabel(), current.getLabel(), result.getDescription());
			}
			
		}
		
		return response;
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
			float similarityIndex = getSimilarityIndex(tag, tagData);
			
			if(similarityIndex == 0){
				continue;
			}
			searchIndex.add(new SemanticSearchIndex(tag, similarityIndex));
			
			resolveTagRelations(tag, searchIndex);
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
				
				SemanticSearchIndex idx = new SemanticSearchIndex(t, 0); // Set similarity index to zero, because we have found this tag through relations 
				if(searchIndex.contains(idx)){
					continue;
				}
				searchIndex.add(idx);
				
				resolveTagRelations(t, searchIndex);
			}
		}
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
	private void addResultList(SemanticSearchResponse response, TaggedEntity taggedEntity, TagData tag, float priority){
		if(taggedEntity == null){
			return;
		}
		
		response.addDetail(taggedEntity.getEntityType(), taggedEntity.getId(), taggedEntity.getDescription(), tag, priority);	
	}
	
	private float getSimilarityIndex(TagData str1, TagData str2){
		
		Set<String> commonWords = Sets.newHashSet("it", "is", "a", "and", "the", "are, i");
		
		StringMetric metric = 
				with(new CosineSimilarity<String>())
				.simplify(Simplifiers.toLowerCase())
				.simplify(Simplifiers.removeNonWord())
				.tokenize(Tokenizers.whitespace())
				.filter(Predicates.not(in(commonWords)))
				.tokenize(Tokenizers.qGram(2))
				.build();
		
		return metric.compare(str1.getTag(), str2.getTag());
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
			code = 89 * code * this.getTag().hashCode();
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