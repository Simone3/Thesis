public class Activity3 extends AppCompatActivity
{
    private TextView countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        countdown = (TextView) findViewById(R.id.email_content);
        
        Button sendEmail = (Button) findViewById(R.id.send_email);
        sendEmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // send email code
            }
        });
    }
}