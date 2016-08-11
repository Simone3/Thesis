public class Activity3 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        TextView emailContent = (TextView) findViewById(R.id.email_content);
		emailContent.addTextChangedListener(new TextWatcher()
		{
            // perform some operation when/before/after text changes
        });
        
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