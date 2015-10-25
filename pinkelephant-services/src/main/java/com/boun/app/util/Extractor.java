package com.boun.app.util;

import java.util.*;
import java.util.regex.Matcher;

public class Extractor {

    public static class Entity {
        public enum Type {
            HASH_TAG, MENTION
        }
        protected int start;
        protected int end;
        protected final String value;
        protected final String listSlug;
        protected final Type type;


        public Entity(int start, int end, String value, String listSlug, Type type) {
            this.start = start;
            this.end = end;
            this.value = value;
            this.listSlug = listSlug;
            this.type = type;
        }

        public Entity(int start, int end, String value, Type type) {
            this(start, end, value, null, type);
        }

        public Entity(Matcher matcher, Type type, int groupNumber) {
            this(matcher, type, groupNumber, -1);
        }

        public Entity(Matcher matcher, Type type, int groupNumber, int startOffset) {
            this(matcher.start(groupNumber) + startOffset, matcher.end(groupNumber), matcher.group(groupNumber), type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Entity)) {
                return false;
            }

            Entity other = (Entity)obj;

            if (this.type.equals(other.type) &&
                    this.start == other.start &&
                    this.end == other.end &&
                    this.value.equals(other.value)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return this.type.hashCode() + this.value.hashCode() + this.start + this.end;
        }

        @Override
        public String toString() {
            return value + "(" + type +") [" +start + "," + end +"]";
        }

        public Integer getStart() {
            return start;
        }

        public Integer getEnd() {
            return end;
        }

        public String getValue() {
            return value;
        }

        public Type getType() {
            return type;
        }

    }

    public Extractor() {}

    private static void removeOverlappingEntities(List<Entity> entities) {

        Collections.<Entity>sort(entities, new Comparator<Entity>() {
            public int compare(Entity e1, Entity e2) {
                return e1.start - e2.start;
            }
        });

        if (!entities.isEmpty()) {
            Iterator<Entity> it = entities.iterator();
            Entity prev = it.next();
            while (it.hasNext()) {
                Entity cur = it.next();
                if (prev.getEnd() > cur.getStart()) {
                    it.remove();
                } else {
                    prev = cur;
                }
            }
        }
    }

    public static List<Entity> extractEntitiesWithIndices(String text) {
        List<Entity> entities = new ArrayList<Entity>();
        entities.addAll(extractHashTagsWithIndices(text));
        entities.addAll(extractMentionsOrListsWithIndices(text));

        removeOverlappingEntities(entities);
        return entities;
    }

    public static List<String> extractMentionedNames(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }

        List<String> extracted = new ArrayList<String>();
        for (Entity entity : extractMentionedNamesWithIndices(text)) {
            extracted.add(entity.value);
        }
        return extracted;
    }


    private static List<Entity> extractMentionedNamesWithIndices(String text) {
        List<Entity> extracted = new ArrayList<Entity>();
        for (Entity entity : extractMentionsOrListsWithIndices(text)) {
            if (entity.listSlug == null) {
                extracted.add(entity);
            }
        }
        return extracted;
    }

    private static List<Entity> extractMentionsOrListsWithIndices(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        boolean found = false;
        for (char c : text.toCharArray()) {
            if (c == '@' || c == '＠') {
                found = true;
                break;
            }
        }
        if (!found) {
            return Collections.emptyList();
        }

        List<Entity> extracted = new ArrayList<Entity>();
        Matcher matcher = Regex.VALID_MENTION_OR_LIST.matcher(text);
        while (matcher.find()) {
            String after = text.substring(matcher.end());
            if (! Regex.INVALID_MENTION_MATCH_END.matcher(after).find()) {
                if (matcher.group(Regex.VALID_MENTION_OR_LIST_GROUP_LIST) == null) {
                    extracted.add(new Entity(matcher, Entity.Type.MENTION, Regex.VALID_MENTION_OR_LIST_GROUP_USERNAME));
                } else {
                    extracted.add(new Entity(matcher.start(Regex.VALID_MENTION_OR_LIST_GROUP_USERNAME) - 1,
                            matcher.end(Regex.VALID_MENTION_OR_LIST_GROUP_LIST),
                            matcher.group(Regex.VALID_MENTION_OR_LIST_GROUP_USERNAME),
                            matcher.group(Regex.VALID_MENTION_OR_LIST_GROUP_LIST),
                            Entity.Type.MENTION));
                }
            }
        }
        return extracted;
    }

    public static List<String> extractHashTags(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }

        List<String> extracted = new ArrayList<String>();
        for (Entity entity : extractHashTagsWithIndices(text)) {
            extracted.add(entity.value);
        }

        return extracted;
    }


    private static List<Entity> extractHashTagsWithIndices(String text) {
        if (text == null || text.length() == 0) {
            return Collections.emptyList();
        }
        boolean found = false;
        for (char c : text.toCharArray()) {
            if (c == '#' || c == '＃') {
                found = true;
                break;
            }
        }
        if (!found) {
            return Collections.emptyList();
        }

        List<Entity> extracted = new ArrayList<Entity>();
        Matcher matcher = Regex.VALID_HASHTAG.matcher(text);

        while (matcher.find()) {
            String after = text.substring(matcher.end());
            if (!Regex.INVALID_HASHTAG_MATCH_END.matcher(after).find()) {
                extracted.add(new Entity(matcher, Entity.Type.HASH_TAG, Regex.VALID_HASHTAG_GROUP_TAG));
            }
        }

        return extracted;
    }
}

