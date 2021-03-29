package Model.Resources;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceContainerTest {

    @Test
    void addQta() {
        ResourceContainer  container = new ResourceContainer(ResourceType.STONE, 4);
        container.addQta(3);
        assertEquals(container.getQta(), 7);
    }

    @Test
    void addNegativeQta() {
        ResourceContainer  container = new ResourceContainer(ResourceType.MINION, 5);
        container.addQta(-2);
        assertEquals(container.getQta(), 3);
    }
    
    @Test
    void testAddWithNegativeOutputQta() {
        assertThrows(ArithmeticException.class, () -> new ResourceContainer(ResourceType.STONE, 2).addQta(-10));
    }

    @Test
    public void testConstructorNegativeQta() {
        assertThrows(ArithmeticException.class, () -> new ResourceContainer(ResourceType.GOLD, -2));
    }

    @Test
    public void testSetNegativeQta() {
        assertThrows(ArithmeticException.class, () -> new ResourceContainer(ResourceType.GOLD, 3).setQta(-3));
    }    
    
    @Test
    void canRemove() {
        ResourceContainer  container1 = new ResourceContainer(ResourceType.MINION, 12);
        ResourceContainer  container2 = new ResourceContainer(ResourceType.MINION, 5);

        assertTrue(container1.canRemove(container2));
        assertFalse(container2.canRemove(container1));
    }

    @Test
    void isTheSameType() {
        ResourceContainer  container1 = new ResourceContainer(ResourceType.MINION, 12);
        ResourceContainer  container2 = new ResourceContainer(ResourceType.MINION, 5);

        assertTrue(container1.isTheSameType(container2));

        container2.setResourceType(ResourceType.GOLD);

        assertFalse(container1.isTheSameType(container2));
    }

    @Test
    void hasEnugh() {
        ResourceContainer  container1 = new ResourceContainer(ResourceType.MINION, 5);
        ResourceContainer  container2 = new ResourceContainer(ResourceType.GOLD, 2);
        container1.addQta(3);

        assertTrue(container1.hasEnough(container2));
        assertFalse(container2.hasEnough(container1));
    }

    
    /*
    @Test
    public void testNegativeSetQta() {
        Throwable exception = assertThrows(
                ArithmeticException.class, () -> {
                    ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 2);
                    container.setQta(-1);
                }
        );
        assertEquals("ResourceContainer can't have a negative qta!", exception.getMessage());
    }*/
}