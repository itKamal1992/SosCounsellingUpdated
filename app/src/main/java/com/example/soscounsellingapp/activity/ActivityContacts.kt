package com.example.soscounsellingapp.activity

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.MediaController
import android.widget.Toast
import com.example.soscounsellingapp.R
import com.example.soscounsellingapp.dashboard.DashboardUser
import com.example.soscounsellingapp.model.APIResponse
import com.example.soscounsellingapp.remote.ApiClientPhp
import com.example.soscounsellingapp.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.layout_post_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ActivityContacts : AppCompatActivity()  {
    var arrayList =  ArrayList<String>(Arrays.asList("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"));
    var index = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)


        val phoneTypeArr = arrayOf("Mobile", "Home", "Work")
        val phoneTypeSpinnerAdaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, phoneTypeArr)
        add_phone_contact_type.adapter = phoneTypeSpinnerAdaptor

        // Click this button to save user input phone contact info.
        // Click this button to save user input phone contact info.

        GetNumber()
        //http://dmimsdu.in/web/test_folder/abc_chart.pdf
        showPdf("http://dmimsdu.in/web/test_folder/abc_chart.pdf")
        post_pdf.visibility=View.GONE

        var mediacontroller = MediaController(this);
        mediacontroller.setAnchorView(post_video);


        post_video.setMediaController(mediacontroller);
        post_video.setVideoURI(Uri.parse(arrayList.get(index)));
        post_video.requestFocus();

        var displaymetrics =  DisplayMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        var h = displaymetrics.heightPixels;
        var w = displaymetrics.widthPixels;

        post_video.getLayoutParams().width = w;
        post_video.getLayoutParams().height = h;



        post_video.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();
            if (index++ == arrayList.size) {
                index = 0;
                it.release();
                Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();
            } else {
                post_video.setVideoURI(Uri.parse(arrayList.get(index)));
                post_video.start();
            }
        })




//      videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Log.d("API123", "What " + what + " extra " + extra);
//                return false;
//            }
//        });



        add_phone_contact_save_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                val addContactsUri = ContactsContract.Data.CONTENT_URI
                // Add an empty contact and get the generated id.
                val rowContactId = getRawContactId()
                // Add contact name data.
                val displayName: String = add_phone_contact_display_name!!.text.toString()
                insertContactDisplayName(addContactsUri, rowContactId, displayName)
                // Add contact phone data.
                val phoneNumber: String = add_phone_contact_number!!.text.toString()
                val phoneTypeStr = add_phone_contact_type.selectedItem as String
                insertContactPhoneNumber(addContactsUri, rowContactId, phoneNumber, phoneTypeStr)
                Toast.makeText(applicationContext, "New contact has been added, go back to previous page to see it in contacts list.", Toast.LENGTH_LONG).show()
//                finish()
            }
        })
    }

    private fun showContacts() { // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 100)
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
//          PERMISSIONS_REQUEST_READ_CONTACTS
        }
//        else { // Android version is lesser than 6.0 or the permission is already granted.
//            val contacts: List<String> = getContactNames()
//            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
//            lstNames.setAdapter(adapter)
//        }
    }

    // This method will only insert an empty data to RawContacts.CONTENT_URI
// The purpose is to get a system generated raw contact id.
    private fun getRawContactId(): Long { // Inser an empty contact.
        val contentValues = ContentValues()
        val rawContactUri: Uri? = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, contentValues)
        // Get the newly created contact raw id.
        return ContentUris.parseId(rawContactUri)
    }


    // Insert newly created contact display name.
    private fun insertContactDisplayName(addContactsUri: Uri, rawContactId: Long, displayName: String) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName)
        contentResolver.insert(addContactsUri, contentValues)
    }

    private fun insertContactPhoneNumber(addContactsUri: Uri, rawContactId: Long, phoneNumber: String, phoneTypeStr: String) { // Create a ContentValues object.
        val contentValues = ContentValues()
        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
        // Calculate phone type by user selection.
        var phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
        if ("home".equals(phoneTypeStr, ignoreCase = true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
        } else if ("mobile".equals(phoneTypeStr, ignoreCase = true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        } else if ("work".equals(phoneTypeStr, ignoreCase = true)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType)
        // Insert new contact data into phone contact list.
        var result=contentResolver.insert(addContactsUri, contentValues)

        println("Content Resolver result : "+result)
    }

    // ListPhoneContactsActivity use this method to start this activity.
    fun start(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }


    fun GetNumber(){
        try {

            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are checking credential")
            dialog.setCancelable(false)
            dialog.show()

            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )
            /*   var call3: Call<ApiVersion> = phpApiInterface.api_version()*/

            var call3: Call<APIResponse> =
                phpApiInterface.GetAllUsers("All_Records")

            call3.enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {

                    // Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    println(" t.message  "+ t.message)


                    val toast = Toast(applicationContext)
                    toast.duration = Toast.LENGTH_LONG

                    val inflater =
                        applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view: View = inflater.inflate(R.layout.toast1, null)
                    toast.view = view
                    toast.show()
                    dialog.dismiss()

                    //  Toast.makeText(applicationContext,"Please enter correct credential", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<APIResponse>,
                    response: Response<APIResponse>
                ) {
                    try {
                        val result: APIResponse? = response.body()
                        var size = response.body()!!.data!!.size
                        println("size arra >>> " + size)

//                        val intent = Intent(this@ActivityContacts, DashboardUser::class.java)
//                        println(" course >>> " + result!!.data!![0].ID)
//
                        dialog.dismiss()
//
//                        startActivity(intent)


                    } catch (ex: Exception) {
                        //    ex.printStackTrace()
                    }
                }
            })


        } catch (ex: Exception) {
            //  ex.printStackTrace()

        }
    }

    fun showPdf(url:String) {
        post_pdf.webViewClient = WebViewClient()
        post_pdf.settings.setSupportZoom(true)
        post_pdf.settings.javaScriptEnabled = true


        post_pdf.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                title = "Loading..."
                setProgress(progress * 100) //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if (progress == 100)
                    setTitle(R.string.app_name)
            }
        })
//        webView.setWebViewClient(HelloWebViewClient())
        post_pdf.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://www.google.com");


        val url = url
        post_pdf.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
    }


}
