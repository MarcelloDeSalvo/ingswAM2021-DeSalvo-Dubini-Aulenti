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
    void canRemove() {
    }

    @Test
    public void testConstructorNegativeQta() {
        assertThrows(ArithmeticException.class, () -> new ResourceContainer(ResourceType.GOLD, -2));
    }

    @Test
    public void testSetNegativeQta() {
        assertThrows(ArithmeticException.class, () -> new ResourceContainer(ResourceType.GOLD, 3).setQta(-3));
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