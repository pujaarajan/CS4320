import java.util.AbstractMap;
import java.util.Map.Entry;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K,T> root;
	public static final int D = 2;

	/**
	 * TODO Search the value for a specific key
	 * 
	 * @param key
	 * @return value
	 */
	public T search(K key) {
		return sTraverse(key, cNode);
	}


	private T sTraverse(K key, Node cNode){
		if( cNode.isLeafNode == true){
			LeafNode leaf = (LeafNode) cNode;
			for(int i = 0; i< leaf.keys.size(); i++ ){
				if( leaf.keys[i]== key){
					return leaf.keys.values[i];
				}
			}
			return null;
		}
		else{
			node = (IndexNode) cNode;
			for(Node<K,T> childNode: cNode ){
				int len = childNode.key.size();
				if (childNode.key[0]<=key && childNode.key[len-1] >key){
					sTraverse(key, cNode);
				} 
			}
		}
	}



	/**
	 * TODO Insert a key/value pair into the BPlusTree
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(K key, T value) {

	}

	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param leaf
	 * @return the key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf) {
		int arrayCount = 0;
		ArrayList <K> keys;
		Arraylist <T> values;
		ArrayList <K> lKeys = leaf.keys;
		LeafNode rNode;
		int splitPoint = (lKeys.size()/2);
		K splitKey;

		for(K key: lKeys){
			if( arrayCount== splitPoint) {
				rNode.LeafNode(key, leaf.values[arrayCount]);
				splitKey= key;
			}
			else if (arrayCount> splitPoint) {
				keys.add(key);
				values.add(leaf.values[arrayCount]);
			}
			arrayCount++;
		}
		rNode.LeafNode(keys, values);
		return Entry(splitKey, rNode);
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index
	 * @return new key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index) {
		int arrayCount = 0;
		ArrayList<K> keys;
		Arraylist Node<K,T> children;
		ArrayList<K> lKeys = leaf.keys;
		IndexNode<K,T> rNode;
		int splitPoint = (lKeys.size()/2);
		K splitKey;

		for(K key, iKeys){
			if( arrayCount == splitPoint){
				splitkey = key;
			}
			else if (arrayCount == (splitPoint+1)){
				rNode.IndexNode(key,index[i],index[i+1]);
			}
			else if(arrayCount> (splitPoint+1) ) {
				keys.add(key);
				children.add(index.children[i]);
			}
			arrayCount++;
		}

		children = Array.copyOfRange(children,1,(children.size()-1));
		children.add(index.children[(index.children.size()-1)]);
		rNode.IndexNode(keys, children );
		return SimpleEntry(splitKey, rNode);

	}

	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {

	}

	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
			IndexNode<K,T> parent) {
		return -1;

	}

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
			IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
		return -1;
	}

}
