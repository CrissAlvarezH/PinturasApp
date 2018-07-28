package pinturas.cristian.com.pinturasapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

public class MainActivity extends AppCompatActivity implements ColorPicker.OnColorChangedListener {

  private final String TAG = "ActividadPrincipal";

  private ColorPicker colorPicker;
  private SVBar barraSaturacionValor;
  private OpacityBar barraOpacidad;
  private SaturationBar barraSaturacion;
  private ValueBar barraValor;
  private TextView txtCodigoRGB, txtCodigoCMYK;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    colorPicker = findViewById(R.id.color_picker);
    barraSaturacionValor = findViewById(R.id.barra_saturacion_valor);
//    barraOpacidad = findViewById(R.id.barra_opacidad);
//    barraSaturacion = findViewById(R.id.barra_saturacion);
//    barraValor = findViewById(R.id.barra_valor);

    txtCodigoRGB = findViewById(R.id.txt_codigo_rgb);
    txtCodigoCMYK = findViewById(R.id.txt_codigo_cmyk);

    // Le asignamos las barras a color picker para que manipulen el color del mismo
    colorPicker.addSVBar(barraSaturacionValor);
//    colorPicker.addSaturationBar(barraSaturacion);
//    colorPicker.addOpacityBar(barraOpacidad);
//    colorPicker.addValueBar(barraValor);


    colorPicker.setOnColorChangedListener(this);
  }

  @Override
  public void onColorChanged(int colorSeleccionado) {

    txtCodigoCMYK.setBackgroundColor( colorSeleccionado );
    txtCodigoRGB.setBackgroundColor( colorSeleccionado );

    int red = Color.red(colorSeleccionado);
    int green = Color.green(colorSeleccionado);
    int blue = Color.blue(colorSeleccionado);

    // Cambiamos el color del texto para que se mantenga visible mientras el fondo cambia de color
    if( (red + green + blue) < 330 ){
      txtCodigoCMYK.setTextColor(Color.WHITE);
      txtCodigoRGB.setTextColor(Color.WHITE);
    }else{
      txtCodigoCMYK.setTextColor(Color.BLACK);
      txtCodigoRGB.setTextColor(Color.BLACK);
    }

    txtCodigoRGB.setText("("+red+", "+green+", "+blue+")");
    int[] cmyk = rgbToCmyk(red, green, blue);

    txtCodigoCMYK.setText("("+cmyk[0]+"%, "+cmyk[1]+"%, "+cmyk[2]+"%, "+cmyk[3]+"%)");

    Log.v(TAG, "Color cambió: "+colorSeleccionado);
  }

  private int[] rgbToCmyk(double red, double green, double blue){
    double cyan, magenta, yellow, black;

    red   = red / 255;
    green = green / 255;
    blue  = blue / 255;

    double maximo = Math.max( Math.max(red, green), blue );

    black = 1 - maximo;

    if(black == 1){
      cyan    = 0;
      magenta = 0;
      yellow  = 0;
    }else{
      cyan    = (1 - red - black) / (1 - black);
      magenta = (1 - green - black) / (1 - black);
      yellow  = (1 - blue - black) / (1 - black);
    }

    // redondeamos para luego pasar a enteros
    cyan = Math.round( cyan * 100 );
    magenta = Math.round( magenta * 100 );
    yellow = Math.round( yellow * 100 );
    black = Math.round( black * 100 );

    return new int[] { (int) cyan, (int) magenta, (int) yellow, (int) black };
  }
}
