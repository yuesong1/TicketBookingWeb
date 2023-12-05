package com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad;

import com.unimelb.swen90007.reactexampleapi.api.mappers.MapperRegistry;
import com.unimelb.swen90007.reactexampleapi.api.objects.PKCounts.Key;
import com.unimelb.swen90007.reactexampleapi.api.objects.Section;

import java.sql.Connection;
import java.util.UUID;

public class SectionLoader implements ValueLoader{
    private UUID sectionId;

    public SectionLoader(UUID id) {
        this.sectionId = id;
    }
    @Override
    public Object load(Connection conn) {
        Section s = (Section) MapperRegistry.getInstance().getMapper(Section.class).find(new Key(sectionId));
        System.out.println(s);
        return s;
    }
}
