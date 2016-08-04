public class Activity4 extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(map!=null)
        {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title("MyMarker"));
        }
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        this.map = map;
    }
}