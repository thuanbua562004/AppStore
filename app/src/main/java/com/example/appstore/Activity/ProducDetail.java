package com.example.appstore.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.Api.CallApi;
import com.example.appstore.Interface.ApiCallback;
import com.example.appstore.Model.Product;
import com.example.appstore.R;

import java.text.NumberFormat;
import java.util.Locale;

import io.github.muddz.styleabletoast.StyleableToast;

public class ProducDetail extends AppCompatActivity {
    ImageView imgProduct ,btnBack ,btnPlus , btnMinus;
    TextView txtPrice , txtInfoProduct,titleTxt,txtNumberProduct ;
    Button btnAddProduct;
    Locale localeVN = new Locale("vi", "VN");
    NumberFormat vn = NumberFormat.getInstance(localeVN);
    RadioGroup radiogr1 ,radiogr2;
    int radio1 , radio2 ;
    int number = 1 ;
    Product product;
    String  id_product;
    CallApi callApi = new CallApi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produc_detail);

        Bundle bundle = new Bundle();
        if (bundle==null){
            return;
        }
        product = (Product) getIntent().getSerializableExtra("product");
         id_product = product.getId_product();

        anhxa();
        hanler();
        getProductDetail(product);
        getSelectNumber();
        addProductToCart();


    }
    private void addProductToCart() {
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio2 = radiogr2.getCheckedRadioButtonId();
                radio1 = radiogr1.getCheckedRadioButtonId();
                if(radio1==-1){
                    StyleableToast.makeText(ProducDetail.this, "Vui lòng chọn màu ! ", Toast.LENGTH_LONG, R.style.success).show();
                    return;
                }
                if(radio2==-1){
                    StyleableToast.makeText(ProducDetail.this, "Vui lòng chọn size !", Toast.LENGTH_LONG, R.style.success).show();
                    return;
                }
                if (radio1 != -1 && radio2 != -1) {
                    RadioButton selectedRadioButton = findViewById(radio1);
                    String selectedText1 = selectedRadioButton.getText().toString();
                    RadioButton selectedRadioButton1 = findViewById(radio2);
                    String selectedText2 = selectedRadioButton1.getText().toString();
                    int slProduct = Integer.valueOf(txtNumberProduct.getText().toString());
                    String uniqueKey = id_product + "_" + selectedText1 + "_" + selectedText2;
                    String name = product.getName();
                    Integer price = (product.getPrice());
                    String img = product.getImg1();
                    SharedPreferences sharedPreferences = getSharedPreferences("AppStore" , MODE_PRIVATE);
                    String userID = sharedPreferences.getString("id","");
                    callApi.addToCart(userID, uniqueKey, selectedText1, id_product, img, name, slProduct, price, selectedText2, new ApiCallback() {
                        @Override
                        public void onSuccess(String response) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StyleableToast.makeText(ProducDetail.this, response, Toast.LENGTH_LONG, R.style.success).show();
                                    Log.i("addtoCart", "run: " +response);
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StyleableToast.makeText(ProducDetail.this, errorMessage, Toast.LENGTH_LONG, R.style.success).show();
                                Log.i("addtoCart", "run: " +errorMessage);
                            }
                        });
                        }
                    });
                }
            }
                });
    }

  private int getSelectNumber() {
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number++;
                updateNumber();
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number==1){
                    return;
                }else{
                    number-- ;
                    updateNumber();
                }

            }
        });
        return 1;
    }

    private void updateNumber() {
        txtNumberProduct.setText(String.valueOf(number));

    }

    private void getProductDetail(Product product) {
        Log.i("product", "getProductDetail: "+ product.toString());
        String text = product.getInfo();
        text = text.replaceAll("\\s{2,}", "\n");
        txtInfoProduct.setText(text.toString());
        txtPrice.setText(String.valueOf(vn.format(product.getPrice())) + " VND");
        titleTxt.setText(product.getName().toString());
        String img = product.getImg1();
        String imgUrl = img.replace("5000", "10.0.2.2:5000");
        String i = imgUrl.replace("localhost:","");
        Glide.with(this)
                .load(i)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(imgProduct);
    }

    private void hanler() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void anhxa() {
        imgProduct = findViewById(R.id.view);
        btnBack = findViewById(R.id.backBtn);
        txtPrice = findViewById(R.id.priceTxt);
        txtInfoProduct =findViewById(R.id.txtview);
        btnAddProduct = findViewById(R.id.addToCartBtn);
        titleTxt = findViewById(R.id.titleTxt);
        txtNumberProduct = findViewById(R.id.numberPro);
        btnMinus = findViewById(R.id.btnminus);
        btnPlus = findViewById(R.id.btnplus);
        radiogr1 = findViewById(R.id.selectColor);
        radiogr2 = findViewById(R.id.selectSize);
    }
}