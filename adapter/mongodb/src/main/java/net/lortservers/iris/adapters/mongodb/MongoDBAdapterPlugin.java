package net.lortservers.iris.adapters.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoClients;
import net.lortservers.iris.config.ConfigurationManagerImpl;
import net.lortservers.iris.managers.ConfigurationManager;
import net.lortservers.iris.utils.profiles.PlayerProfileManager;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.Plugin;
import org.screamingsandals.lib.utils.annotations.PluginDependencies;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Optional;

@Plugin(
        id = "Iris-MongoDBAdapter",
        name = "Iris-MongoDBAdapter",
        version = "0.0.1-SNAPSHOT",
        authors = {"zlataovce", "Lort533"}
)
@PluginDependencies(platform = PlatformType.BUKKIT, dependencies = {
        "Iris"
})
@PluginDependencies(platform = PlatformType.MINESTOM, dependencies = {
        "Iris"
})
@PluginDependencies(platform = PlatformType.SPONGE, dependencies = {
        "Iris"
})
public class MongoDBAdapterPlugin extends PluginContainer {
    private static final ConfigurationManager.FileDefinition<MongoDBAdapterConfig> CONFIG = ConfigurationManagerImpl.FileDefinitionImpl.of(MongoDBAdapterConfig.class, "mongodb.json");

    @Override
    public void enable() {
        CONFIG.load();
        if (!CONFIG.toFile().exists()) {
            CONFIG.save();
        }
        final String connectionUri = getConfigValue("connectionUri", String.class).orElse("");
        final String databaseName = getConfigValue("databaseName", String.class).orElse("iris");
        final boolean isValid = isNamespaceValid(databaseName);
        if (!isValid) {
            getLogger().error("Invalid database name, cannot continue.");
        }
        if (!connectionUri.equals("") && isValid && !PlayerProfileManager.hasAdapter()) {
            PlayerProfileManager.setAdapter(
                    new MongoDBPersistenceAdapter(
                            MongoClients.create(
                                    MongoClientSettings.builder()
                                            .applyConnectionString(new ConnectionString(connectionUri))
                                            .codecRegistry(CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())))
                                            .build()
                            ).getDatabase(databaseName)
                    )
            );
        }
    }

    private <T> Optional<T> getConfigValue(String key, Class<T> returnType) {
        return (CONFIG.getObject() == null) ? Optional.empty() : Optional.of(returnType.cast(Reflect.getField(CONFIG.getObject(), key)));
    }

    private boolean isNamespaceValid(String name) {
        try {
            MongoNamespace.checkDatabaseNameValidity(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
