package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exceptions.NotEnoughResources;
import it.polimi.ingsw.model.resources.ResourceContainer;
import it.polimi.ingsw.model.resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class  VaultTest {

    @Test
    void TestAddToVault() {
        Vault vault = new Vault(true);
        ResourceContainer container1 = new ResourceContainer(ResourceType.GOLD, 5);
        ResourceContainer container2 = new ResourceContainer(ResourceType.STONE, 5);
        ResourceContainer container3 = new ResourceContainer(ResourceType.GOLD, 2);

        vault.addToVault(container1);
        assertEquals(vault.getVaultMap().get(ResourceType.GOLD).getQty(), 5 );

        vault.addToVault(container2);
        assertEquals(vault.getResourceQuantity(ResourceType.STONE),5);

        vault.addToVault(container3);
        assertEquals(vault.getResourceQuantity(ResourceType.GOLD),7 );

    }

    @Test
    void getNotExistingResourceFromVault(){
        Vault vault = new Vault(true);
        assertThrows(NullPointerException.class, ()-> vault.getVaultMap().get(ResourceType.SHIELD).getQty());
        assertEquals(vault.getResourceQuantity(ResourceType.SHIELD),0 );

    }


    @Test
    void removeFromVault_1() {
        Vault vault = new Vault(true);
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 3);
        vault.removeFromVault(cardPrice);

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 2);
    }

    @Test
    void removeFromVault_2() {
        Vault vault = new Vault(true);
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 10 );
        assertThrows(ArithmeticException.class,()->vault.removeFromVault(cardPrice));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 5);
    }


    @Test
    void canRemoveFromVault_1() {
        Vault vault = new Vault(true);
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 10 );
        assertThrows(NotEnoughResources.class, ()->vault.canRemoveFromVault(cardPrice));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 5);

    }


    @Test
    void canRemoveFromVault_2() {
        Vault vault = new Vault(true);
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 4 );
        assertAll(()->vault.removeFromVault(cardPrice));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 1);

    }

    @Test
    void canRemoveFromVault_3_List() {
        Vault vault = new Vault(true);
        ArrayList<ResourceContainer> selectedResources = new ArrayList<ResourceContainer>();

        ResourceContainer vContainer1 = new ResourceContainer(ResourceType.GOLD, 5);
        ResourceContainer vContainer2 = new ResourceContainer(ResourceType.STONE, 3);
        ResourceContainer vContainer3 = new ResourceContainer(ResourceType.MINION, 4);

        vault.addToVault(vContainer1);
        vault.addToVault(vContainer2);
        vault.addToVault(vContainer3);


        ResourceContainer resourceContainer1 = new ResourceContainer(ResourceType.GOLD, 5);
        ResourceContainer resourceContainer2 = new ResourceContainer(ResourceType.STONE, 3);
        ResourceContainer resourceContainer3 = new ResourceContainer(ResourceType.MINION, 4);

        selectedResources.add(resourceContainer1);
        selectedResources.add(resourceContainer2);
        selectedResources.add(resourceContainer3);

        assertAll(()->vault.canRemoveFromVault(selectedResources));
        assertAll(()->vault.removeFromVault());

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 0);
        assertEquals(vault.getResourceQuantity(ResourceType.STONE), 0);
        assertEquals(vault.getResourceQuantity(ResourceType.MINION), 0);

    }

    @Test
    void canRemoveFromVault_4_List() {
        Vault vault = new Vault(true);
        ArrayList<ResourceContainer> selectedResources = new ArrayList<ResourceContainer>();

        ResourceContainer vContainer1 = new ResourceContainer(ResourceType.GOLD, 5);
        ResourceContainer vContainer2 = new ResourceContainer(ResourceType.STONE, 3);

        vault.addToVault(vContainer1);
        vault.addToVault(vContainer2);


        ResourceContainer resourceContainer1 = new ResourceContainer(ResourceType.GOLD, 5);
        ResourceContainer resourceContainer2 = new ResourceContainer(ResourceType.STONE, 4);

        selectedResources.add(resourceContainer1);
        selectedResources.add(resourceContainer2);

        assertThrows(NotEnoughResources.class, ()->vault.canRemoveFromVault(selectedResources));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 5);
        assertEquals(vault.getResourceQuantity(ResourceType.STONE), 3);

    }

    @Test
    void canRemoveFromVault_5() throws NotEnoughResources {
        Vault vault = new Vault(true);
        ArrayList<ResourceContainer> selectedResources = new ArrayList<ResourceContainer>();

        ResourceContainer vContainer1 = new ResourceContainer(ResourceType.MINION, 5);
        ResourceContainer vContainer2 = new ResourceContainer(ResourceType.STONE, 3);

        vault.addToVault(vContainer1);
        vault.addToVault(vContainer2);

        ResourceContainer resourceContainer1 = new ResourceContainer(ResourceType.MINION, 2);
        ResourceContainer resourceContainer2 = new ResourceContainer(ResourceType.MINION, 1);

        assertTrue(vault.canRemoveFromVault(resourceContainer1));
        assertEquals(2, vault.getBufferMap().get(ResourceType.MINION).getQty());

        assertTrue(vault.canRemoveFromVault(resourceContainer1));
        assertEquals(4, vault.getBufferMap().get(ResourceType.MINION).getQty());

        assertThrows(NotEnoughResources.class, ()-> vault.canRemoveFromVault(resourceContainer1));
        assertThrows(NotEnoughResources.class, ()-> vault.canRemoveFromVault(new ResourceContainer(ResourceType.STONE, 4)));

        assertTrue(vault.canRemoveFromVault(resourceContainer2));

        assertTrue(vault.removeFromVault());
        assertEquals(0, vault.getResourceQuantity(ResourceType.MINION));
    }

    @Test
    void totalQuantityOfResourcesInVault(){
        Vault vault = new Vault(true);
        assertEquals(0,vault.totalQuantityOfResourcesInVault());
        vault.addToVault(new ResourceContainer(ResourceType.GOLD,2));
        assertEquals(2,vault.totalQuantityOfResourcesInVault());
        vault.addToVault(new ResourceContainer(ResourceType.MINION,3));
        assertEquals(5,vault.totalQuantityOfResourcesInVault());
        vault.addToVault(new ResourceContainer(ResourceType.MINION,4));
        assertEquals(9,vault.totalQuantityOfResourcesInVault());
        vault.addToVault(new ResourceContainer(ResourceType.STONE,60));
        assertEquals(69,vault.totalQuantityOfResourcesInVault());

    }

}