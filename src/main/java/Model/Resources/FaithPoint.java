package Model.Resources;
import Model.FaithPath;

public class FaithPoint extends Resource{
    ResourceType resourceType;

    public FaithPoint() {
        super (ResourceType.FAITHPOINT);
    }

    @Override
    public boolean addToFaithPath(FaithPath faithPath) {
        return super.addToFaithPath(faithPath);
    }
}