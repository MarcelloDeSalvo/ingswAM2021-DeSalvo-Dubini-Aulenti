package Model.Resources;

public class ResourceContainer {
    private ResourceType resourceType;

    /**
     * Quantity of resources inside the container
     */
    private int qta;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getQta() {
        return qta;
    }

    public void setQta(int qta) {
        this.qta = qta;
    }
}
