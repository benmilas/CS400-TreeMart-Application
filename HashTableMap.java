// --== CS400 File Header Information ==--
// Name: Ben Milas
// Email: bmilas@wisc.edu
// Team: Red
// Group: KE
// TA: Keren Chen
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Represents a hash table which stores a collection of generic KeyTypes and ValueTypes that
 * implements the MapADT to efficiently store and find data
 * 
 * @author Ben Milas
 *
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  private LinkedList<HashNode>[] array; // array of linked lists which store keys and values
  private int capacity;
  private int size;
  private final int DEFAULT_HASHTABLE_CAPACITY = 10;
  private final double MAX_LOAD_FACTOR = 0.85;

  /**
   * A private wrapper class that holds the KeyType, ValueType, and next HashNode associated with
   * each HashTable entry to provide LinkedList functionality.
   *
   */
  private class HashNode {
    private KeyType key;
    private ValueType value;
    private HashNode next;

    /**
     * Constructor that builds an entry for the LinkedList that holds the KeyType, ValueType, and
     * next HashNode in the LinkedList
     * 
     * @param key
     * @param value
     * @param next
     */
    public HashNode(KeyType key, ValueType value, HashNode next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }

  /**
   * Constructor that initializes the hash table with a chosen, positive capacity
   * 
   * @param capacity the max indices of the hash table
   */
  public HashTableMap(int capacity) {
    if (capacity < 1)
      throw new IllegalArgumentException("Capacity must be positive");

    array = (LinkedList<HashNode>[]) new LinkedList[capacity];
    this.capacity = capacity;
    size = 0;
  }

  /**
   * Constructor that initializes the hash table with a default capacity of 10
   */
  public HashTableMap() {
    array = (LinkedList<HashNode>[]) new LinkedList[DEFAULT_HASHTABLE_CAPACITY];
    capacity = DEFAULT_HASHTABLE_CAPACITY;
    size = 0;
  }

  /**
   * Inserts a key-value pair into the hash table if the key is not null or already in use
   * 
   * @param key   the key associated with the HashNode
   * @param value the value associated with the HashNode
   * @return true if the HashNode was successfully inserted, false otherwise
   */
  @Override
  public boolean put(KeyType key, ValueType value) {
    if (key == null)
      return false;

    HashNode newNode = new HashNode(key, value, null);
    int index = getIndex(key);
    // case 1: index generated from key is currently not in use (null)
    if (array[index] == null) {
      array[index] = new LinkedList<HashNode>();
      array[index].add(newNode);
      size++;
      // check if array needs to rehash
      if (isArrayTooFull())
        rehash();
      return true;
    }
    // case 2: index is in use, need to check to make sure key isn't in use before insertion
    else {
      HashNode previousNode = null;
      HashNode currentNode = array[index].get(0);
      while (currentNode != null) {
        if (currentNode.key.equals(key))
          return false;

        previousNode = currentNode;
        currentNode = currentNode.next;
      }
      if (previousNode != null) {
        previousNode.next = newNode;
        size++;
        // check if array needs to rehash
        if (isArrayTooFull())
          rehash();
      }
    }
    return true;
  }

  /**
   * Getter method that returns the value associated with a specific search key if it is found
   * 
   * @param key the key that is being searched for
   * @return the ValueType associated with a key, if that key exists within the hash table
   * @throws NoSuchElementException if the key is not contained within the hash table
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    if (key == null)
      throw new NoSuchElementException("Key cannot be null.");

    int index = getIndex(key);
    // check if index is in use
    if (array[index] != null) {
      HashNode hashNode = array[index].get(0);
      while (hashNode != null) {
        if (hashNode.key.equals(key)) {
          ValueType value = hashNode.value;
          return value;
        }
        hashNode = hashNode.next;
      }
    }
    throw new NoSuchElementException("Key is not stored in HashTable.");
  }

  /**
   * Returns the number of key-value pairs stored in this collection
   * 
   * @return the number of key-value pairs stored in this collection
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Checks whether a key is contained within the hash table by using the get() function.
   * 
   * @return true if the key is contained within the hash table, false otherwise
   */
  @Override
  public boolean containsKey(KeyType key) {
    try {
      get(key);
      return true;
    } catch (NoSuchElementException nsee) {
      return false;
    }
  }

  /**
   * Removes an element from the hash table by searching for its key
   * 
   * @return the ValueType associated with the key that was removed from the hash table, or null if
   *         the key is null or doesn't exist within the hash table
   */
  @Override
  public ValueType remove(KeyType key) {
    if (key == null)
      return null;

    int index = getIndex(key);
    // check if index is in use
    if (array[index] != null) {
      HashNode previousNode = null;
      HashNode currentNode = array[index].get(0);
      while (currentNode != null) {
        if (currentNode.key.equals(key)) {
          // key is the first element in the index's LinkedList
          if (previousNode == null) {
            ValueType toReturn = currentNode.value;
            currentNode = currentNode.next;
            array[index].set(0, currentNode);
            size--;
            return toReturn;
          } // key isn't the first element: need to change previous element's next HashNode
          else {
            ValueType toReturn = currentNode.value;
            previousNode.next = currentNode.next;
            size--;
            return toReturn;
          }
        }
        previousNode = currentNode;
        currentNode = currentNode.next;
      }
    }
    return null;
  }


  /**
   * Clears out the array by setting all the indices to null, and resetting the size to 0
   */
  @Override
  public void clear() {
    for (int i = 0; i < capacity; i++) {
      if (array[i] != null)
        array[i] = null;
    }
    size = 0;
  }

  /**
   * Returns the capacity of the hash table
   * 
   * @return the capacity of the hash table
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * Makes a copy of the old array of values, and rehashes them over to a new array with double the
   * capacity
   */
  private void rehash() {
    LinkedList<HashNode>[] oldArray = array;
    array = (LinkedList<HashNode>[]) new LinkedList[capacity * 2];
    int oldArrayCapacity = this.getCapacity();
    size = 0;
    capacity *= 2;
    for (int i = 0; i < oldArrayCapacity; i++) {
      // avoid empty array indices
      if (oldArray[i] == null)
        continue;

      HashNode currentNode = oldArray[i].getFirst();
      // rehashes all the elements from the linked list associated with the index, if applicable
      while (currentNode != null) {
        KeyType key = currentNode.key;
        ValueType value = currentNode.value;

        put(key, value);
        currentNode = currentNode.next;
      }
    }
  }

  /**
   * Gets the index representation of the key's positive hashCode modulo the array's capacity
   * 
   * @param key the key being converted to an index
   * @return the index representation of the key
   */
  private int getIndex(KeyType key) {
    return Math.abs(key.hashCode()) % capacity;
  }

  /**
   * Checks whether the array is too full, and if a rehash is necessary
   * 
   * @return true if the loadFactor is greater than the MAX_LOAD_FACTOR, false otherwise
   */
  private boolean isArrayTooFull() {
    double loadFactor = size / (double) capacity;
    if (Double.compare(loadFactor, MAX_LOAD_FACTOR) >= 0)
      return true;

    return false;
  }

}
