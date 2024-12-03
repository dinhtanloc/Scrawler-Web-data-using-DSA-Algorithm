package ueh.util;

public class Queue<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return front.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public static void main(String[] args) {
        Queue<Integer> queue = new Queue<>();

        // Thêm các phần tử vào Queue
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        // Kiểm tra kích thước Queue
        System.out.println("Queue size: " + queue.size()); // 3

        // Xem phần tử đầu tiên
        System.out.println("Front element: " + queue.peek()); // 1

        // Lấy phần tử ra
        System.out.println("Dequeued: " + queue.dequeue()); // 1
        System.out.println("Queue size after dequeue: " + queue.size()); // 2

        // Xem phần tử đầu tiên sau khi dequeue
        System.out.println("Front element: " + queue.peek()); // 2
    }
}