package Model.Resources;

import Model.Exceptions.DifferentResourceType;
import Model.Exceptions.NotEnoughResources;

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

    public boolean canRemove(ResourceContainer container) throws DifferentResourceType, NotEnoughResources {
        if(!this.resourceType.equals(container.getResourceType()))
            throw new DifferentResourceType("Not the same type");
        else if(this.getQta() < container.getQta())
            throw new NotEnoughResources("Not enough resources");
        else
            return true;
    }

    public boolean isTheSameType(ResourceContainer container) throws DifferentResourceType
    {
        if(!this.resourceType.equals(container.getResourceType()))
            throw new DifferentResourceType("Not the same type");
        else
            return true;
    }

    public boolean hasEnough(ResourceContainer container) throws NotEnoughResources
    {
        if(this.getQta() < container.getQta())
            throw new NotEnoughResources("Not enough resources");
        else
            return true;
    }
}
