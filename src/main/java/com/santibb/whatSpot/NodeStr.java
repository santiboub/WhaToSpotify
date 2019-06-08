package com.santibb.whatSpot;
public class NodeStr
{
    private String  data;
    private NodeStr next;
    private NodeStr previous;


    public NodeStr( String data ) 
    {
        this( null, data, null );
    }

    public NodeStr( String data, NodeStr next )
    {
        this( null, data, next );
    }
    public NodeStr( NodeStr previous, String data, NodeStr next )
    {
        this.previous = previous;
        this.data = data;
        this.next = next;
    }

    public String getData() { return data; }
    public NodeStr getNext() { return next; }
    public NodeStr getPrevious() { return previous; }

    public void setData( String newValue ) { data = newValue; }
    public void setNext( NodeStr newNext ) { next = newNext; }
    public void setPrevious( NodeStr newPrevious ) { previous = newPrevious; }


    @Override
    public String toString()
    {
        return String.format( " %s", data );
    }
}
