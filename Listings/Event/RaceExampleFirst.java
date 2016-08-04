public class Activity1 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        new MyAsyncTask().execute("parameter1", "parameter2");
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String>
    {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(Activity1.this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            String operationResult = null;
			
			/* Perform some background operation, e.g. download image from the Internet */

            return operationResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            progressDialog.dismiss();
        }
    }
}