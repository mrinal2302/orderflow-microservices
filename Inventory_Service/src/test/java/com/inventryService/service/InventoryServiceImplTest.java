package com.inventryService.service;

import com.inventryService.entity.InventoryEntity;
import com.inventryService.feign.NotificationClient;
import com.inventryService.globalExceptionHandler.WentOutOfStockException;
import com.inventryService.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    private InventoryEntity mockInventory;

    @Mock
    private NotificationClient notificationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockInventory = new InventoryEntity(1L, "Table", 10, 4500.0, "a good quality table");
    }

    @Test
    void testGetValueSaveIn() {
        when(inventoryRepository.save(mockInventory)).thenReturn(mockInventory);
        InventoryEntity result = inventoryService.getValueSaveIn(mockInventory);
        assertEquals(mockInventory, result);
    }


    @Test
    void testGetValueSaveIn_FailedSave() {
        when(inventoryRepository.save(mockInventory)).thenReturn(null);
        InventoryEntity result = inventoryService.getValueSaveIn(mockInventory);
        assertNull(result);
    }

    @Test
    void testGetValueSaveIn_Exception() {
        when(inventoryRepository.save(mockInventory)).thenThrow(new RuntimeException("Database error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.getValueSaveIn(mockInventory));
        assertEquals("Database error", ex.getMessage());
    }


    @Test
    void testGetInventoryById() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(mockInventory));
        InventoryEntity result = inventoryService.getInventoryById(1L);
        assertEquals(mockInventory, result);

    }

    @Test
    void testGetInventoryById_NotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> inventoryService.getInventoryById(1L));
    }

    @Test
    void testGetInventoryById_Exception() {
        when(inventoryRepository.findById(1L)).thenThrow(new RuntimeException("DB connection lost"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.getInventoryById(1L));
        assertEquals("DB connection lost", ex.getMessage());
    }


    @Test
    void testGetAllInventoryId() {
        List<InventoryEntity> list = List.of(mockInventory);
        when(inventoryRepository.findAll()).thenReturn(list);
        List<InventoryEntity> result = inventoryService.getAllinventoryId();
        assertEquals(1, result.size());
    }
    @Test
    void testGetAllInventoryId_Empty() {
        when(inventoryRepository.findAll()).thenReturn(Collections.emptyList());
        List<InventoryEntity> result = inventoryService.getAllinventoryId();
        assertNotEquals(List.of(mockInventory), result);
    }

    @Test
    void testGetAllInventoryId_Exception() {
        when(inventoryRepository.findAll()).thenThrow(new RuntimeException("DB unavailable"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.getAllinventoryId());
        assertEquals("DB unavailable", ex.getMessage());
    }


    @Test
    void testUpdateInventoryById_Positive() {
        when(inventoryRepository.findById(101L)).thenReturn(Optional.of(mockInventory));
        when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(mockInventory);
        InventoryEntity result = inventoryService.updateInventoryById(101L, mockInventory);
        assertNotNull(result);
    }

    @Test
    void testUpdateInventoryById_NotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> inventoryService.updateInventoryById(1L, mockInventory));
        assertNotEquals("Some other message", ex.getMessage());
    }


    @Test
    void testUpdateInventoryById_Exception() {
        when(inventoryRepository.findById(1L)).thenThrow(new RuntimeException("Query timeout"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.updateInventoryById(1L, mockInventory));
        assertEquals("Query timeout", ex.getMessage());
    }

    @Test
    void testDelInventoryById() {
        doNothing().when(inventoryRepository).deleteById(1L);
        inventoryService.delInventoryBYId(1L);
        verify(inventoryRepository).deleteById(1L);
    }

    @Test
    void testDelInventoryById_NotFound() {
        doThrow(new RuntimeException("Not found")).when(inventoryRepository).deleteById(99L);
        assertThrows(RuntimeException.class, () -> inventoryService.delInventoryBYId(99L));
    }

    @Test
    void testDelInventoryById_Exception() {
        doThrow(new RuntimeException("Delete failed")).when(inventoryRepository).deleteById(1L);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.delInventoryBYId(1L));
        assertEquals("Delete failed", ex.getMessage());
    }

    @Test
    void testFindByProductNameStartingWithIgnoringCase() {
        List<InventoryEntity> results = List.of(mockInventory);
        when(inventoryRepository.findByProductNameStartingWithIgnoringCase("Tab")).thenReturn(results);
        List<InventoryEntity> response = inventoryService.findByProductNameStartingWithIgnoringCase("Tab");
        assertEquals(1, response.size());
    }

    @Test
    void testFindByProductNameStartingWithIgnoringCase_NoMatch() {
        when(inventoryRepository.findByProductNameStartingWithIgnoringCase("XYZ")).thenReturn(Collections.emptyList());
        assertTrue(inventoryService.findByProductNameStartingWithIgnoringCase("XYZ").isEmpty());
    }

    @Test
    void testFindByProductNameStartingWithIgnoringCase_Exception() {
        when(inventoryRepository.findByProductNameStartingWithIgnoringCase("Tab")).thenThrow(new RuntimeException("Query failed"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.findByProductNameStartingWithIgnoringCase("Tab"));
        assertEquals("Query failed", ex.getMessage());
    }


    @Test
    void testInventorySuccessById_Positive() {
        when(inventoryRepository.findById(101L)).thenReturn(Optional.of(mockInventory));
        Optional<InventoryEntity> result = inventoryService.inventorySuccessById(101L);
        assertTrue(result.isPresent(), "Expected inventory entity to be present");
    }


    @Test
    void testInventorySuccessById_Negative_NotFound() {
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<InventoryEntity> result = inventoryService.inventorySuccessById(2L);
        assertTrue(result.isEmpty(), "Expected no inventory entity for invalid id");
    }

    @Test
    void testInventorySuccessById_Exception() {
        when(inventoryRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.inventorySuccessById(1L));
        assertEquals("DB error", ex.getMessage());
    }


    @Test
    void testWentOutOfStock_Positive() {
        when(inventoryRepository.findById(101L)).thenReturn(Optional.of(mockInventory));
        Optional<InventoryEntity> result = inventoryService.wentOutOfStock(101L);
        assertTrue(result.isPresent(), "Expected inventory entity to be present");
    }

    @Test
    void testWentOutOfStock_Negative_NotFound() {
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(WentOutOfStockException.class, () -> inventoryService.wentOutOfStock(2L));
    }

    @Test
    void testWentOutOfStock_Exception() {
        when(inventoryRepository.findById(1L)).thenThrow(new RuntimeException("DB connection lost"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.wentOutOfStock(1L));
        assertEquals("DB connection lost", ex.getMessage());
    }

    @Test
    void testSendById_Positive() {
        when(inventoryRepository.findById(101L)).thenReturn(Optional.of(mockInventory));
        when(notificationClient.sendById(any())).thenReturn("Notification Sent");
        String result = inventoryService.sendById(101L);
        assertEquals("Notification Sent", result);
    }

    @Test
    void testSendById_Negative_NotFound() {
        when(inventoryRepository.findById(2L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.sendById(2L));
        assertNotEquals("Some other error", ex.getMessage());
    }

    @Test
    void testSendById_ExceptionFromNotificationClient() {
        when(inventoryRepository.findById(101L)).thenReturn(Optional.of(mockInventory));
        when(notificationClient.sendById(any())).thenThrow(new RuntimeException("Feign call failed"));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.sendById(101L));
        assertEquals("Feign call failed", ex.getMessage());
    }
}