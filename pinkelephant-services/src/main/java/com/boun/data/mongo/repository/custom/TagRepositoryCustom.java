package com.boun.data.mongo.repository.custom;

import com.boun.data.mongo.model.Tag;
import com.boun.http.request.TagData;

public interface TagRepositoryCustom {

	public Tag findTag(TagData tagData);
}
