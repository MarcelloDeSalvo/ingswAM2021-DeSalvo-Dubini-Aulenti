package model.player;

import model.resources.ResourceContainer;
import model.resources.ResourceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountSiteTest {

    @Test
    void getDiscount() {
        DiscountSite discountSite = new DiscountSite();
        ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 2);

        assertEquals(0, discountSite.getDiscount(ResourceType.MINION));
        assertEquals(0, discountSite.getDiscount(container.getResourceType()));

        discountSite.addDiscount(container);

        assertEquals(2, discountSite.getDiscount(container.getResourceType()));

        discountSite.addDiscount(container);

        assertEquals(4, discountSite.getDiscount(container.getResourceType()));
    }

    @Test
    void getDiscount2() {
        DiscountSite discountSite = new DiscountSite();
        ResourceContainer container = new ResourceContainer(ResourceType.GOLD, 0);

        discountSite.addDiscount(container);

        assertEquals(0, discountSite.getDiscount(container.getResourceType()));
    }

    @Test
    void addDiscount() {
        DiscountSite discountSite = new DiscountSite();
        ResourceContainer container = new ResourceContainer(ResourceType.STONE, 3);
        ResourceContainer container2 = new ResourceContainer(ResourceType.SHIELD, 1);
        ResourceContainer container3 = new ResourceContainer(ResourceType.SHIELD, 2);

        assertTrue(discountSite.addDiscount(container));
        assertEquals(3, discountSite.getDiscount(container.getResourceType()));

        assertTrue(discountSite.addDiscount(container2));
        assertEquals(1, discountSite.getDiscount(container2.getResourceType()));

        assertTrue(discountSite.addDiscount(container3));
        assertTrue(discountSite.addDiscount(container3));
        assertEquals(5, discountSite.getDiscount(container3.getResourceType()));
    }
}