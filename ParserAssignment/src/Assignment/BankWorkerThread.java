package Assignment;

public class BankWorkerThread implements Runnable {
	  
    private String command;
    ProcessXML processXML;
    
    public BankWorkerThread(String s){
    	this.command=s;
    	processXML = new ProcessXML();
        
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
        try {
            Thread.sleep(7000);
            processXML.processXMLFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}