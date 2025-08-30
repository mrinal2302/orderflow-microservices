package com.inventryService.controllerProject;

import com.inventryService.entity.InventoryEntity;
import com.inventryService.service.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryControllerTest {

    @InjectMocks
    private InventoryController controller;

    @Mock
    private InventoryService service;

    private InventoryEntity mockinventory1;
    private InventoryEntity mockinventory2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockinventory1 = new InventoryEntity(101L, "Table", 10, 4500.0, "this is a good quality table");
        mockinventory2 = new InventoryEntity(102L, "Chair", 20, 3500.0, "this is perfect office chair");
    }

    @Test
    void testGetValueSaveIn() {
        when(service.getValueSaveIn(mockinventory1)).thenReturn(mockinventory1);
        ResponseEntity<InventoryEntity> response = controller.getValueSaveIn(mockinventory1);
        assertEquals(mockinventory1, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetValueSaveIn_Exception() {
        when(service.getValueSaveIn(mockinventory1))
                .thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> {
            controller.getValueSaveIn(mockinventory1);
        });
    }

    @Test
    void testGetInventoryById() {
        when(service.getInventoryById(101L)).thenReturn(mockinventory1);
        ResponseEntity<InventoryEntity> response = controller.getInventoryById(101L);
        assertEquals(mockinventory1, response.getBody());
    }

    @Test
    void testGetInventoryById_NullReturn() {
        when(service.getInventoryById(101L)).thenReturn(null);
        ResponseEntity<InventoryEntity> response = controller.getInventoryById(101L);
        assertNull(response.getBody());
    }

    @Test
    void testGetInventoryById_Exception() {
        when(service.getInventoryById(101L))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> {
            controller.getInventoryById(101L);
        });
    }

    @Test
    void testGetAllInventoryId() {
        when(service.getAllinventoryId()).thenReturn(List.of(mockinventory1, mockinventory2));
        ResponseEntity<List<InventoryEntity>> response = controller.getAllinventoryId();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllInventoryId_Negative_EmptyList() {
        when(service.getAllinventoryId()).thenReturn(Collections.emptyList());
        ResponseEntity<List<InventoryEntity>> response = controller.getAllinventoryId();
        assertNotNull(response);
    }

    @Test
    void testGetAllInventoryId_Exception() {
        when(service.getAllinventoryId()).thenThrow(new RuntimeException("Database Not Found error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            controller.getAllinventoryId();
        });
        assertEquals("Database Not Found error", ex.getMessage());
    }

    @Test
    void testUpdateInventoryById() {
        when(service.updateInventoryById(eq(101L), any(InventoryEntity.class))).thenReturn(mockinventory1);
        ResponseEntity<InventoryEntity> response = controller.updateInventoryById(101L, mockinventory1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockinventory1, response.getBody());
    }

    @Test
    void testUpdateInventoryById_Negative_NullReturn() {
        when(service.updateInventoryById(eq(101L), any(InventoryEntity.class))).thenReturn(null);
        ResponseEntity<InventoryEntity> response = controller.updateInventoryById(101L, mockinventory1);
        assertNotNull(response);
    }

    @Test
    void testUpdateInventoryById_Exception() {
        when(service.updateInventoryById(eq(101L), any(InventoryEntity.class))).thenThrow(new RuntimeException("Update failed"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            controller.updateInventoryById(101L, mockinventory1);
        });
        assertEquals("Update failed", ex.getMessage());
    }

    @Test
    void testDelInventory() {
        doNothing().when(service).delInventoryBYId(101L);
        controller.delInventory(101L);
        verify(service, times(1)).delInventoryBYId(101L);
    }

    @Test
    void testDelInventory_Negative_NoEntityFound() {
        doNothing().when(service).delInventoryBYId(101L);
        controller.delInventory(101L);
        verify(service, times(1)).delInventoryBYId(101L);
    }

    @Test
    void testDelInventory_Exception() {
        doThrow(new RuntimeException("Delete failed")).when(service).delInventoryBYId(101L);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            controller.delInventory(101L);
        });
        assertEquals("Delete failed", ex.getMessage());
    }

    @Test
    void testFindByProductNameStartingWithIgnoringCase() {
        when(service.findByProductNameStartingWithIgnoringCase("Tab")).thenReturn(List.of(mockinventory1));

        ResponseEntity<List<InventoryEntity>> response = controller.findByProductNameStartingWithIgnoringCase("Tab");
        assertEquals(302, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testFindByProductName_Negative_NoResults() {
        when(service.findByProductNameStartingWithIgnoringCase("XYZ")).thenReturn(Collections.emptyList());
        ResponseEntity<List<InventoryEntity>> response = controller.findByProductNameStartingWithIgnoringCase("XYZ");
        assertNotNull(response);
    }

    @Test
    void testFindByProductName_Exception() {
        when(service.findByProductNameStartingWithIgnoringCase("Laptop")).thenThrow(new RuntimeException("Database error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            controller.findByProductNameStartingWithIgnoringCase("Laptop");
        });
        assertEquals("Database error", ex.getMessage());
    }


    @Test
    void testInventorySuccessById_PositiveScenario() {
        Long id = 1L;
        InventoryEntity mockEntity = new InventoryEntity();
        mockEntity.setProductId(101L);
        mockEntity.setProductName("Table");

        Mockito.when(service.inventorySuccessById(id)).thenReturn(Optional.of(mockEntity));

        ResponseEntity<Optional<InventoryEntity>> response = controller.inventorySuccessById(id, new InventoryEntity());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testInventorySuccessById_Negative_EmptyOptional() {
        when(service.inventorySuccessById(101L)).thenReturn(Optional.empty());
        ResponseEntity<Optional<InventoryEntity>> response = controller.inventorySuccessById(101L, mockinventory1);
        assertNotNull(response);
    }

    @Test
    void testInventorySuccessById_Exception() {
        when(service.inventorySuccessById(101L)).thenThrow(new RuntimeException("Inventory success failed"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            controller.inventorySuccessById(101L, mockinventory1);
        });
        assertEquals("Inventory success failed", ex.getMessage());
    }


    @Test
    void testWentOutOfStock_Success() {
        InventoryEntity entity = new InventoryEntity();
        entity.getProductId();
        when(service.wentOutOfStock(1L)).thenReturn(Optional.of(entity));
        ResponseEntity<Optional<InventoryEntity>> response = controller.wentOutOfStock(1L);
        assertTrue(response.getBody().isPresent());

    }


    @Test
    void testWentOutOfStock_Negative_NotFound() {
        when(service.wentOutOfStock(99L)).thenReturn(Optional.empty());
        ResponseEntity<Optional<InventoryEntity>> response = controller.wentOutOfStock(99L);
        assertNotEquals(HttpStatus.OK, response.getBody());

    }

    @Test
    void testWentOutOfStock_ExceptionThrown() {
        when(service.wentOutOfStock(101L)).thenThrow(new RuntimeException("availability error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            controller.wentOutOfStock(101L);
        });
        assertEquals("availability error", ex.getMessage());

    }

    @Test
    void testSendById_Positive() {
        Long id = 101L;
        when(service.sendById(id)).thenReturn("Item Found");
        ResponseEntity<String> response = controller.sendById(id);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testSendById_Negative() {
        Long id = 2L;
        when(service.sendById(id)).thenReturn(null);
        ResponseEntity<String> response = controller.sendById(id);
        assertNull(response.getBody());
    }

    @Test
    void testSendById_Exception() {
        Long id = 3L;
        when(service.sendById(id)).thenThrow(new RuntimeException("Service failed"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.sendById(id));
        assertEquals("Service failed", ex.getMessage());
    }
}