import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.Arrays;


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
		return sTraverse(key, root);
	}


	private T sTraverse(K key, Node cNode){
		if( cNode.isLeafNode == true){
			LeafNode<K,T> leaf = (LeafNode) cNode;
			for(int i = 0; i< leaf.keys.size(); i++ ){
				if( leaf.keys.get(i)== key){
					return leaf.values.get(i);
				}
			}
			return null;
		}
		else{
			IndexNode<K,T> node = (IndexNode) cNode;
			for(Node childNode: node.children ){
				int len = childNode.keys.size();
				if ((childNode.keys.get(0).compareTo(key) !=0) && (childNode.keys.get(len-1).compareTo(key) >0)){
					sTraverse(key, childNode);
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
		insertHelp(key,value,root);

	}

	public Entry<K,Node<K,T>> insertHelp(K key, T value, Node<K,T> n){

		int i = 0;
		if(n.isLeafNode){
			for(K fKey : n.keys){
				if( (fKey.compareTo(key))<0 ){
					n.keys.set(i,fKey);
					if( n.isOverflowed()){
						splitLeafNode( (LeafNode) n);
					}
					return (new AbstractMap.SimpleEntry<K, Node<K,T>>(null,null))  ;
				}
				i++;
			}
		}
		else{
			for(K nKey: n.keys){
				if(nKey.compareTo(key)<0){
					AbstractMap.SimpleEntry<K, Node<K,T>> entry =  insertHelp(key,value, n.children[i+1] );
					if( entry.getKey != null){
						 n.keys.set(entry.key,i);
						 n.children.set(entry.value, i+1);
						if(n.isOveflowed()){
							splitIndexNode(n);
						}
					}
				}
			}
		}
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
				keys.set(key);
				values.set(leaf.values[arrayCount]);
				splitKey= key;
			}
			else if (arrayCount> splitPoint) {
				keys.set(key);
				values.set(leaf.values[arrayCount]);
			}
			arrayCount++;
		}

		leaf.keys = Array.copyOfRange(leaf.keys,0, (splitPoint-1));
		leaf.values = Array.copyOfRange(leaf.values,0, (splitPoint-1));

		rNode.LeafNode(keys, values);
		return new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rNode);
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
		ArraylistNode<K,T> children;
		ArrayList<K> lKeys = leaf.keys;
		IndexNode<K,T> rNode;
		int splitPoint = (lKeys.size()/2);
		K splitKey;

		for(K key: iKeys){
			if( arrayCount == splitPoint){
				splitkey = key;
			}
			else if (arrayCount == (splitPoint+1)){
				rNode.IndexNode(key,index[i],index[i+1]);
			}
			else if(arrayCount> (splitPoint+1) ) {
				keys.set(key);
				children.set(index.children[i]);
			}
			arrayCount++;
		}

		children = Array.copyOfRange(children,1,(children.size()-1));
		children.set(index.children[(index.children.size()-1)]);
		rNode.IndexNode(keys, children );
		return new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rNode);

	}
    


	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {

		deleteHelp(key, n, null);


	}


	public void deleteHelp( K key, Node<K,T> n, Node<K,T> parent){
		
		int k =0;
		if (n.isLeafNode){
			for( int i=0; i< n.keys.size()-1 ; i++ ){
				if(n.keys[i] == key){
					n.keys.remove(i);
					n.values.remove(i);
					if( n.isUnderflowed()){
						int result = handleLeafNodeUnderflow(n.previosleaf, n, parent );
						if(result != -1 && parent!=null){
							parent.remove(result);							
						}
					}
				}
			}
		}
	else{
		for(K fKey: n.keys){
			if(fKey> key){
				if(n.isUnderflowed()){ 
					int result = handleIndexNodeOverflow ();
						if( result != -1 && parent!= null){
							parent.remove(result);
						}
					deleteHelp(K,n.children[k+1], n);
				}
			}
			k++;
		}
	}
}



	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * isUnderflowed
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


		int leftSize = left.keys.size();
		int rightSize = right.key.size();
		int i;

		if ((leftSize+rightSize) <= (2*D) ){
			left.keys = ArrayUtils.setAll(left.keys, right.keys);
			left.values =ArrayUtils.setAll(left.values, right.values);
			
			left.nextLeaf= right.nextLeaf;

			for(K nKey: parent.keys){
				if(nKey>left.keys[leftSize-1] ){
					return i;
				}
				i++;
			}

		}

		int splitPoint = (D/2);

		int  sizeInd = (rightSize-1);
		int distAmount = sizeInd-splitPoint;

		for(i=0; i< distAmount; i++ ){
			left.keys.set(right.keys[i]);
			left.values.set(right.values[i]);
		}

		K distKey = right.keys[0];

		for(K fKey: parent.keys){
				if(fKey>left.keys[leftSize-1] ){
				parent.keys[i] = distKey;				
			}
 		}

		right.keys = Array.copyOfRange(right.keys, splitPoint-1, sizeInd-1);
		right.values = Array.copyOfRange(right.values, splitPoint-1, sizeInd-1);

		left.keys.set( distKey);
		left.values.set(distVal);

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
		
		int leftSize = leftIndex.keys.size();
		int rightSize =  rightIndex.keys.size();
		int parSize = parent.keys.size();
		int parentKey;

		for(K nKey: parent.keys){
			if(nKey>left.keys[leftSize-1] ){
				parentkey = i;
			}
		}

		if( (leftSize+rightSize+1)<=(2*d)){

			leftIndex.keys = ArrayUtils.setAll(leftIndex.keys, rightIndex.keys);
			leftIndex.children = ArrayUtils.setAll(leftIndex.children,  Array.copyOfRange(rightIndex.children, 1, (rightSize-1))  );				
			return parentKey;
		}
		



		K distKey = right.keys[0];
		V distChild = right.values[0];
		splitPoint = (D/2);

		left.keys.set(parentKey[i]);

		int distAmount = sizeInd-splitPoint;

		for(i=0; i< distAmount; i++ ){
			left.keys.set(right.keys[i]);
			left.children.set(right.children[i]);
			right.keys.remove(0);
			right.children.remove(0);
		}

		parent.keys[i] = right.keys[0];
		return -1;
		}
	

}
