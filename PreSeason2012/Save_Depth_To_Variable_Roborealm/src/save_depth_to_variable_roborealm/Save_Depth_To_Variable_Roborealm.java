/* 
 * Remember to turn off RGB and Infrared,
 * Set to raw depth instead of grayscale,
 * Set min threshhold to 0.
 */

int width = 640;
int height = 480;

unsigned char *pixels = getPixels(&width, &height);
//Take the 12 red bits.
unsigned int red = pixels[width*height*3/2-width*3/2-1];

//Take the 4 green bits.
unsigned int green = pixels[width*height*3/2-width*3/2+1];
green<<=8;
//Add them together.
float total = green+red;
//Magic Equation.
total = 1.0 / (total * -0.0030711016 + 3.3309495161);
//Save it to available variables as a float.
setFloatVariable("TOTAL", total);

printf("Total = %f \n", total);
printf("Red = %d \n", red);
printf("Green = %d \n", pixels[width*height*3/2-width*3/2+1]);
