package ExtractInterface;

public interface QueueAvecPrioriteI {

	boolean add(Object o);

	boolean isEmpty();

	Object peek();

	Object poll();

	Object comparator();

}