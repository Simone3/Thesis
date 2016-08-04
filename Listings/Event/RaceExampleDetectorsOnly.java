public class Activity2 extends AppCompatActivity
{
    private MyService service = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        
        Button button = (Button) findViewById(R.id.perform_operation);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(service!=null)
                {
                    int num = service.doSomeOperation();
                    Toast.makeText(Activity2.this, "Result: "+num, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Activity2.this, "Wait for the service to start", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        
        if(service==null)
        {
            bindService(new Intent(this, MyService.class), connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(service!=null)
        {
            unbindService(connection);
        }
    }

    private ServiceConnection connection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            Activity2.this.service = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            Activity2.this.service = null;
        }
    };
}