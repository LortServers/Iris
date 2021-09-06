package net.iris.ac;

import net.iris.ac.checks.CriticalsCheckA;
import org.screamingsandals.lib.plugin.PluginContainer;
import org.screamingsandals.lib.utils.annotations.Init;
import org.screamingsandals.lib.utils.annotations.Plugin;

@Plugin(
        id = "Iris",
        authors = {"zlataovce", "Lort533"},
        version = "1.0-SNAPSHOT"
)
@Init(services = {
        CriticalsCheckA.class
})
public class IrisPlugin extends PluginContainer {
    @Override
    public void enable() {

    }
}
