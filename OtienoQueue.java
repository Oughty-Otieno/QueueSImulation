package assignment;

public class OtienoQueue {

final static int Q_LIMIT =100;
static int next_event_type=1;
static int num_custs_delayed=0;
static int num_delays_required;
static int num_events;
static int num_in_q=0;
static int server_status=0;
static float area_num_in_q=0;
static float area_server_status=0;
static float mean_interarrival=1;
static float mean_service=(float) 0.5;
static float simtime=0;
static float time_arrival[] = new float[Q_LIMIT+1];
static float time_next_event[]=new float [3];
static float time_last_event;
static float total_of_delays;
public static void initialize ()//initialization function
{     

  time_next_event[1]=(float) (simtime+Math.exp(mean_interarrival));
  time_next_event[2]=(float) 1.0e+30;
}
 
public static void timing ()//timing function
{
    float min_time_next_event = (float) 1.0e+30; 
    //determine the event of the next event to occur
    for(int i=1;i<=num_events;++i){
        if (time_next_event[i]<min_time_next_event){
            min_time_next_event=time_next_event[i];
            next_event_type=i;
    }
}
    //check to see whether the evnt list is empty
    if(next_event_type==0){
    	System.out.println("the simulation stops due to que empty");
        //the event list is empty so stop the simulation
    return;    
    }
    //the event list is not empty so advance the simulation clock
             
    simtime=min_time_next_event; 
    }
     
public static void arrival ()//arrival event function
{
    float delay;
    //schedule next arrival
    time_next_event[1]=simtime+(float)(simtime+expon(mean_interarrival));
    //int BUSY;
    //check to see whether server is busy
    if (server_status==1){
        //server is busy so increment the number of customers in queue
        ++num_in_q;
        //check to see whether an overflow condition exists
        if (num_in_q>Q_LIMIT){
        	System.out.println("Overflow of array so the simulation stops");
            //the queue has overflowed so stop the simulation
            return;
        }
        /*there is still room in the queue so store the time of arrival of
         * the arriving customer at the (new)and of time arrival
         */
        time_arrival[num_in_q]=simtime;
    }else{
        /*server is idle so arriving customer has a delay of zero(the following 
         * two statements are for program clarity and do not affect the results
         * of the simulation
         */
        delay=(float) 0.0;
        total_of_delays+=delay;
        //increment the number of customers delayed and make server busy
        ++num_custs_delayed;
        server_status=1;
        //schedule a departure(service completion)
        time_next_event[2]=simtime+(float) (simtime+expon(mean_service));
    }
 
}
public static void depart()//departure event function
{
    float delay;
    //check to see whether the queue is empty
    if(num_in_q==0){
        //int IDLE = 0;
        /*the queue is empty so make the server idle and eliminate the departure
         * (service completion)event from consideration
         */
        server_status=0;
        time_next_event[2]=(float) 1.0e+30;
    }else{
        //the queue is nonempty so decrement the number of customers in queue
        --num_in_q;
        //float[] time_arrival = null;
        /*compute the delay of the customer who is beginning service and update
         * the total delay accumulator
         */
        delay=simtime-time_arrival[1];
        total_of_delays+=delay;
        //increment the number of customers delayed and schedule departure
        ++num_custs_delayed;
        time_next_event[2]=(float) (simtime+expon(mean_service));
        //move each customer in queue(if any) up on one place
        for(int i=1;i<=num_in_q;++i){
            time_arrival[i]=time_arrival[i+1];
        	
        }
    }
}
public static float expon(float mean){
	return (float) (-mean*(Math.log(Math.random())));
	
}
public static void report ()//report generator function
{
	System.out.println("welcome to the simulation");
	System.out.println("The average delay in the queue is: "+(total_of_delays/num_custs_delayed));
	System.out.println("Time-Average number in the queue is :"+(area_num_in_q/simtime));
	System.out.println("Server utilization Percentage:"+((area_server_status/simtime)*100));
	System.out.println("time simulation ended:"+simtime);
	
    
}
public static void update_time_avg_stats()//update area accumulators for time-average statistics
{
    float time_since_last_event=0;
    //compute time since last event and update last_event_time marker
    time_since_last_event=simtime-time_last_event;
    time_last_event=simtime;
    //update area under number_in_queue function
    area_num_in_q+=num_in_q *time_since_last_event;
    //update area under server busy indicator function
    area_server_status+=server_status*time_since_last_event;
}
public static void main(String[] args) {
    // specify the number of events for the timing function
    num_events=2;
    initialize();
    //run the simulation while more delays are still needed
    while(num_custs_delayed<90){
        //determine the next event
        timing();
        //update time average statistical accumulators
        update_time_avg_stats();
        //invoke the appropriate event function
        switch(next_event_type){
            case 1:
                arrival();
           break;
            case 2:
                depart();
                break;
        }
    }
    //invoke the report generator and end the simulation
    report();
        }
}


