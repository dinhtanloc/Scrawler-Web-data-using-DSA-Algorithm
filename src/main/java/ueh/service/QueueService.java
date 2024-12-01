package ueh.service;

import ueh.model.HtmlData;
import org.springframework.stereotype.Service;
import ueh.util.Queue;

@Service
public class QueueService {
    private final Queue<HtmlData> htmlQueue;

    public QueueService() {
        this.htmlQueue = new Queue<>();
    }

    public void enqueue(HtmlData htmlData) {
        htmlQueue.enqueue(htmlData);
    }

    public HtmlData dequeue() {
        return htmlQueue.dequeue();
    }
}
