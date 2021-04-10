package Model.Resources;

import javax.print.DocFlavor;

public class ResourceContainer {

    private ResourceType resourceType;

    /**
     * Quantity of resources inside the container
     */
    private int qty;

    public ResourceContainer(ResourceType resourceType, int qty) throws ArithmeticException {

        if(qty < 0)
            throw new ArithmeticException("ResourceContainer can't have a negative qty!");
        else {
            this.resourceType = resourceType;
            this.qty = qty;
        }
    }


    public void addQty(int n) {
        setQty(this.qty + n);
    }

    /*public void subQty(int n) {
        if(this.qty < n)
            throw new ArithmeticException("ResourceContainer can't have a negative qty");
        else
            this.qty = this.qty - n;
    }*/


    public boolean canRemove(ResourceContainer container) {
        return this.isTheSameType(container) && this.hasEnough(container);
    }

    public boolean isTheSameType(ResourceContainer container) {
        return this.resourceType == container.getResourceType();
    }

    public boolean hasEnough(ResourceContainer container) {
        return this.getQty() >= container.getQty();
    }

    public boolean isEmpty(){
        return this.qty == 0;
    }

    public boolean isEditable(){ return this.resourceType == null; }


    //getter and setter

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) throws ArithmeticException {
        if(qty < 0)
            throw new ArithmeticException("ResourceContainer can't have a negative qty");
        else
            this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceContainer)) return false;
        ResourceContainer container = (ResourceContainer) o;
        return qty == container.qty && resourceType == container.resourceType;
        
    }

    @Override
    public String toString() {
        return "ResourceContainer{" +
                "resourceType=" + resourceType +
                ", qty=" + qty +
                '}';
    }
}
