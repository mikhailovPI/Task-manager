package manager.history;

import manager.history.Node;
import manager.interfaceClass.HistoryManager;
import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head; // Указатель на первый элемент списка
    private Node tail; //Указатель на последний элемент списка
    private int size = 0; //Размер списка
    protected Map<Long, Node> historyHashMap = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(long id) {
        Node node = historyHashMap.get(id);
        if (node != null) {
            removeNode(node);
            historyHashMap.remove(id);
        }
    }

    public void linkLast(Task task) {
        if (historyHashMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        size++;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        historyHashMap.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyList.add(node.data);
            node = node.next;
        }
        return historyList;
    }

    public void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.data = null;
        size--;
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "head=" + head +
                ", tail=" + tail +
                ", size=" + size +
                ", historyHashMap=" + historyHashMap +
                '}';
    }
}