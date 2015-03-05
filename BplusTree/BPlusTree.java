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
		insertHelp(key,value,root);

	}

	public Entry<K,Node<K,T>> insertHelp(K key, T value, Node<K,T> n){

		int i = 0;
		if(n.isLeafNode){
			for(K fKey : n.keys){
				if( fKey < key ){
					n.keys.add(fKey, i);
					if( n.isOveflowed()){
						splitLeafNode(n);
					}
					return AbstractMap.SimpleEntry(null,null)  ;
				}
				i++;
			}
		}
		else{
			for(K nKey: keys){
				if(fKey< key){
					Map.Entry<K, Node<K,T>> entry =  insertHelp(key,value, n.children[i+1] );
					if( entry.getKey != null){
						 n.keys.add(entry.key,i);
						 n.children.add(entry.value, i+1);
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
				keys.add(key);
				values.add(leaf.values[arrayCount]);
				splitKey= key;
			}
			else if (arrayCount> splitPoint) {
				keys.add(key);
				values.add(leaf.values[arrayCount]);
			}
			arrayCount++;
		}

		leaf.keys = Array.copyOfRange(leaf.keys,0, (splitPoint-1));
		leaf.values = Array.copyOfRange(leaf.values,0, (splitPoint-1));

		rNode.LeafNode(keys, values);
		return AbstractMap.SimpleEntry(splitKey, rNode);
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
				keys.add(key);
				children.add(index.children[i]);
			}
			arrayCount++;
		}

		children = Array.copyOfRange(children,1,(children.size()-1));
		children.add(index.children[(index.children.size()-1)]);
		rNode.IndexNode(keys, children );
		return AbstractMap.SimpleEntry(splitKey, rNode);

	}

	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {

		for( int i=0; i< root.size()-1 ; i++ ){
			if ()
		}

			if key.isUnderflowed {
				if key.isLeafNode{
					//handleLeafNodeUnderflow()
				}
				else if key.isIndexNode{
					//handleIndexNodeOverflow()
				}










				//handleLeafNodeUnderflow

			}


	}


	public void deleteHelp( K key, Node<K,T> n, Node<K,T> parent){
		
		if (n.isLeafNode){
			for( int i=0; i< n.keys.size()-1 ; i++ ){
				if(n.keys[i] == key){
					n.keys.remove[i];
					n.values.remove[i];
					if( n.isUnderflowed()){
						handleLeafNodeUnderflow(n.previosleaf, n, parent );
					}
				}
			}
		}

	else{
		for( i=0; i< n.keys.size()-2 ; i++ ){
			if( n.keys[i]<key && (n.keys+1>= key)){
				deleteHelp(K )
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

		if ((leftSize+rightSize) <= (2*D) ){
			Arraylist<K,T> newChild;
			left.IndexNode(ArrayUtils.addAll(left.keys, right.keys), ArrayUtils.addAll(left.values, right.values));

			left.nextLeaf= right.nextLeaf;

			for(int i = 0; i<parent.keys.size(); i++){
				if(parent.keys[i]>left.keys[0] && parent.keys[i]>=left.keys[0]){
					return parent.keys[i];
				}
			}

		}

		int  sizeInd = (rightSize-1);

		K distKey = right.keys[0];
		V distVal = right.values[0];

		right.keys = Array.copyOfRange(right.keys, 1, sizeInd)
		right.values = Array.copyOfRange(right.values, 1, sizeInd);

		left.keys.add( distKey);
		left.values.add(distVal);


		for (i=0; i < parent.keys.size(), i++){
			if(parent.keys[i] == distKey){
				parent.keys[i] = distKey;				
			}
 		}

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

		for (int i =0; i<parsize;i+=){
			if( (parent.keys[i]<= right.keys[0]) && (parent.keys[i]>left.keys[(leftSize-1)]){
				parentkey = i;
			}
		}

		if( (leftSize+rightSize<=(2*d)){

			leftIndex.keys = ArrayUtils.addAll(leftIndex.keys, rightIndex.keys);
			leftIndex.children = ArrayUtils.addAll(leftIndex.children,  Array.copyOfRange(rightIndex.children, 1, (rightSize-1)  );				
			return parentKey;
		}
		


		K distKey = right.keys[0];
		V distChild = right.values[0];
		splitPoint = (D/2);

		left.keys.add(parentKey[i], 0 )

		for( int k = 0; i< splitPoint; i++){
			left.keys.add(right.keys[i],0);
			left.children.add(right.children[],0);
			right.keys.remove(0);
			right.children.remove(0);
		}

		parent.keys[i] = rightIndex[splitPoint];
		}
	}

}
