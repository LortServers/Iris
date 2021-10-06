package net.lortservers.iris.adapters.mongodb;

import lombok.*;

@EqualsAndHashCode
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MongoDBAdapterConfig {
    private String connectionUri = "";
    private String databaseName = "iris";
}
