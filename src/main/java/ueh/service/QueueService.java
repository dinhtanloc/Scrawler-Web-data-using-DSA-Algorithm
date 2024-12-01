package ueh.service;


import ueh.model.HtmlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
public class QueueService {
    @Autowired
    private BlockingQueue<HtmlData> htmlQueue;

    public void enqueue(HtmlData htmlData) throws InterruptedException {
        htmlQueue.put(htmlData);
    }

    public HtmlData dequeue() throws InterruptedException {
        return htmlQueue.take();
    }
}
