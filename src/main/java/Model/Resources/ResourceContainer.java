package Model.Resources;

public class ResourceContainer {
    private ResourceType resourceType;

    /**
     * Quantity of resources inside the container
     */
    private int qta;

    public ResourceContainer(ResourceType resourceType, int qta) throws ArithmeticException {
        if(qta < 0)
            throw new ArithmeticException("ResourceContainer can't have a negative qta!");
        else{
            this.resourceType = resourceType;
            this.qta = qta;
        }
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getQta() {
        return qta;
    }

    public void setQta(int qta) throws ArithmeticException {
            if(qta < 0)
                throw new ArithmeticException("ResourceContainer can't have a negative qta!");
            else
                this.qta = qta;
    }

    public void addQta(int n) {
        this.qta = this.qta + n;
    }

    public boolean canRemove(ResourceContainer container) {
        return this.isTheSameType(container) && this.hasEnough(container);
    }

    public boolean isTheSameType(ResourceContainer container) {
        return this.resourceType.equals(container.getResourceType());
    }

    public boolean hasEnough(ResourceContainer container) {
        return this.getQta() >= container.getQta();
    }
}
