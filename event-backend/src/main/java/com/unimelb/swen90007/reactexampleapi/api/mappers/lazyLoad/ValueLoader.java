package com.unimelb.swen90007.reactexampleapi.api.mappers.lazyLoad;

import java.sql.Connection;

public interface ValueLoader {
     Object load(Connection conn);
}
