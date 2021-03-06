package com.aliyun.tauris.config.parser;

import com.aliyun.tauris.TPlugin;
import com.aliyun.tauris.TPluginFactory;
import com.aliyun.tauris.TPluginGroup;
import com.aliyun.tauris.config.TConfigException;
import com.aliyun.tauris.TPluginResolver;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jdziworski on 30.03.16.
 */
public class PluginGroup {
    private final String       typeName;
    private final List<Plugin> plugins;

    private Assignments assignments;

    public PluginGroup(String typeName, List<Plugin> plugins, Assignments assignments) {
        this.typeName = typeName;
        this.plugins = plugins;
        this.assignments = assignments;
    }

    public String typeName() {
        return typeName;
    }

    public TPluginGroup build(Class<? extends TPluginGroup> groupClass) {
        TPluginFactory factory = TPluginResolver.defaultResolver.resolvePluginFactory(typeName);
        Helper.m.text(typeName).expand("{").next();
        List<TPlugin> plugins = this.plugins.stream().map(plugin -> plugin.build(factory)).collect(Collectors.toList());
        try {
            TPluginGroup g = groupClass.getConstructor(List.class).newInstance(plugins);
            this.assignments.assignTo(g);
            String pid = "filter_" + RandomStringUtils.randomNumeric(8).toLowerCase();
            if (g.id() == null) {
                g.setId(pid);
                Helper.m.collapse("} //id: " + pid).next();
            } else {
                Helper.m.collapse("}").next();
            }
            return g;
        } catch (TConfigException e) {
            throw e;
        } catch (Exception e){
            throw new IllegalArgumentException("invalid group class '" + groupClass.getName() + "', cause by " + e.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginGroup aClass = (PluginGroup) o;

        if (typeName != null ? !typeName.equals(aClass.typeName) : aClass.typeName != null) return false;
        return plugins != null ? plugins.equals(aClass.plugins) : aClass.plugins == null;
    }

    @Override
    public int hashCode() {
        int result = typeName != null ? typeName.hashCode() : 0;
        result = 31 * result + (plugins != null ? plugins.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\t\ttype='" + typeName + '\'' + "\n" +
                "\t\tplugins=" + plugins + "\n" +
                '}';
    }
}
