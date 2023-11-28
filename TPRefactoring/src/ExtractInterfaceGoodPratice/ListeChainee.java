package ExtractInterfaceGoodPratice;

class ListeChainee implements IListe, IQueueChainee {
	
public boolean add(Object o) {return true;}
public boolean isEmpty() {return true;}
public Object get(int i) {return null;}
public Object peek() {return null;}
public Object poll() {return null;}
private void secretLC(){}

}