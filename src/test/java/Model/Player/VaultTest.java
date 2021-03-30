package Model.Player;

import Model.Exceptions.NotEnoughResources;
import Model.Resources.ResourceContainer;
import Model.Resources.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class  VaultTest {

    @Test
    void TestAddToVault() {
        Vault vault = new Vault();
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
    void getNotExistingResourceFromVolt(){
        Vault vault = new Vault();
        assertThrows(NullPointerException.class, ()-> vault.getVaultMap().get(ResourceType.SHIELD).getQty());
        assertEquals(vault.getResourceQuantity(ResourceType.SHIELD),0 );

    }


    @Test
    void removeFromVault_1() {
        Vault vault = new Vault();
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 3);
        vault.removeFromVault(cardPrice);

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 2);
    }

    @Test
    void removeFromVault_2() {
        Vault vault = new Vault();
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 10 );
        assertThrows(ArithmeticException.class,()->vault.removeFromVault(cardPrice));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 5);
    }


    @Test
    void canRemoveFromVault_1() {
        Vault vault = new Vault();
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 10 );
        assertThrows(NotEnoughResources.class, ()->vault.canRemoveFromVault(cardPrice));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 5);

    }


    @Test
    void canRemoveFromVault_2() {
        Vault vault = new Vault();
        ResourceContainer resourceContainer = new ResourceContainer(ResourceType.GOLD, 5);
        vault.addToVault(resourceContainer);

        ResourceContainer cardPrice = new ResourceContainer(ResourceType.GOLD, 4 );
        assertAll(()->vault.removeFromVault(cardPrice));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 1);

    }

    @Test
    void canRemoveFromVault_3_Array() {
        Vault vault = new Vault();
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
        assertAll(()->vault.removeFromVault(selectedResources));

        assertEquals(vault.getResourceQuantity(ResourceType.GOLD), 0);
        assertEquals(vault.getResourceQuantity(ResourceType.STONE), 0);
        assertEquals(vault.getResourceQuantity(ResourceType.MINION), 0);

    }

    @Test
    void canRemoveFromVault_4_Array() {
        Vault vault = new Vault();
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

}