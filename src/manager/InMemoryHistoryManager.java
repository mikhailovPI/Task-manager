package manager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    //LinkedList<Task> history = new LinkedList<>();
    //private final static int SIZE_HISTORY = 10;

    private Node head = null; // Указатель на первый элемент списка
    Map<Integer, Node> mapNode = new HashMap<>();

    private Node tail = null; //Указатель на последний элемент списка
    private int size = 0; //Размер списка

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        removeNode(newNode);
        linkLast(task);

/*        history.add(task);
        if (history.size() > SIZE_HISTORY) {
            history.removeFirst();
        }*/
    }

    @Override
    public void remove(int id) {
        Node node = mapNode.get(id);
        if (node != null) {
            // history.removeNode(node);
            mapNode.remove(id);
        }
    }

    void removeNode(Node node) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, node.data, null);
        tail = newNode;
        if (mapNode.containsKey(node.data.getId())) {
            mapNode.remove(node.data.getId());
            if (mapNode.containsKey(node.data.getId() - 1)) {
                if (mapNode.containsKey(node.data.getId() + 1)) { // мы в середине
                    newNode.prev = newNode.next;
                    newNode.next = oldTail;
                } else { // мы в хвосте
                    newNode.prev = tail;
                    newNode.next = null;
                }
            } else { // мы в голове
                newNode.prev = null;
                newNode.next = head;
            }
        }
    }

    private void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        size++;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        mapNode.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node node = tail;
        while (node != null) {
            historyList.add(node.data);
            node = node.prev;
        }
        return historyList;
    }
}

class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}

