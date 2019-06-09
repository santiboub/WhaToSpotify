package com.santibb.whatSpot;
public class QueueStrLinked
{
    private NodeStr    first;
    private NodeStr    last;
    private int        size;

    public QueueStrLinked() 
    {
        first = last = null;
        size = 0;
    }
    public void add( String value ) // T(n) in O(1)
        throws Exception
    {
        if ( isEmpty() ) {
            first = last = new NodeStr(value);
        } else {
            last.setNext( new NodeStr(value) );
            last = last.getNext();
        }
        ++size;
    }
    public String remove() // T(n) in O(1)
        throws Exception
    {
        if ( isEmpty() ) throw new Exception( "Queue empty!" );
        
        String temp = first.getData();
        if ( first == last ) {
            first = last = null;
        } else {
            first = first.getNext();
        }
        --size;
        
        return temp;
    }
    public String first() // T(n) in O(1)
        throws Exception
    {
        if ( isEmpty() ) throw new Exception( "Queue empty!" );

        return first.getData();
    }
    public int size() // T(n) in O(1)
    {
        return size;
    }
    public boolean isEmpty() // T(n) in O(1)
    {
        return size() == 0;
    }
    public QueueStrLinked split_half() {
    	QueueStrLinked nd = new QueueStrLinked();
    	int nds = this.size/2;
    	if (nd.size < nds) {			
    		try {
				nd.add(this.remove());
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return nd;
    }
	public boolean contains(String adder) {
        NodeStr n = first;
        while( n != null && !n.getData().equals(adder)) {
            n=n.getNext();
        }
        return (n != null);
	}
}
