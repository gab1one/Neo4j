/**
 *
 */
package se.redfield.knime.neo4jextension.cfg;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Vyacheslav Soldatov <vyacheslav.soldatov@inbox.ru>
 *
 */
public class ConnectorPortData {
    private ConnectorConfig config;

    private List<String> nodeLabels = new LinkedList<>();
    private List<String> relationshipTypes = new LinkedList<>();
    private List<String> propertyKeys = new LinkedList<String>();

    public ConnectorPortData() {
        config = new ConnectorConfig();
    }

    public void setConnectorConfig(final ConnectorConfig cfg) {
        this.config = cfg;
    }
    public ConnectorConfig getConnectorConfig() {
        return config;
    }
    public List<String> getNodeLabels() {
        return nodeLabels;
    }
    public List<String> getRelationshipTypes() {
        return relationshipTypes;
    }
    public void setNodeLabels(final List<String> nodeLabels) {
        this.nodeLabels = nodeLabels;
    }
    public void setRelationshipTypes(final List<String> relationshipTypes) {
        this.relationshipTypes = relationshipTypes;
    }
    public void setPropertyKeys(final List<String> keys) {
        this.propertyKeys = keys;
    }
    public List<String> getPropertyKeys() {
        return propertyKeys;
    }
}