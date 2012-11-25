int width = 640;
int height = 480;
unsigned char *pixels = getPixels(&width, &height);

unsigned int red = pixels[width*height*3/2-width*3/2-1];
unsigned int green = pixels[width*height*3/2-width*3/2+1];
green<<=8;
float total = green+red;
total = 1.0 / (total * -0.0030711016 + 3.3309495161);
setFloatVariable("TOTAL", total);

printf("Total = %f \n", total);
printf("Red = %d \n", red);
printf("Green = %d \n", pixels[width*height*3/2-width*3/2+1]);
