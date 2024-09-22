package com.example.appstore.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appstore.Model.Cart;
import com.example.appstore.Model.Product;
import com.example.appstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    String  check ="" , id_product;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produc_detail);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = new Bundle();
        if (bundle==null){
            return;
        }
        product = (Product) getIntent().getSerializableExtra("product");
         id_product =  getIntent().getStringExtra("id_product");

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
                    Cart cart_product = new Cart(product.getName(), Integer.valueOf(id_product), slProduct, selectedText2, selectedText1,product.getImg1(),product.getPrice());

                    String id_user_isidCart = user.getUid();
                    String uniqueKey = id_product + "_" + selectedText1 + "_" + selectedText2;

                    isCheckSizeColor(id_user_isidCart, cart_product, id_user_isidCart, uniqueKey);
                }
            }
                });
    }

    private void isCheckSizeColor(String idCart, Cart cart ,String id_Cart , String checkSizeColor) {
        DatabaseReference cartRef = mDatabase.child("cart").child(id_Cart + "/" + checkSizeColor);
        cartRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        showAlerDialog("Sản Phẩm Đã Có Trong Giỏ Hàng!");
                    } else {
                        mDatabase.child("cart/"+idCart+"/"+checkSizeColor).setValue(cart);
                        showAlerDialog("Thêm Thành Công!");                    }
                } else {
                    Log.e("FirebaseError", "Failed to get data: " + task.getException());
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
        Glide.with(this)
                .load(product.getImg1())
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
    private void  showAlerDialog(String mess){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo! ");
        builder.setMessage(mess);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}