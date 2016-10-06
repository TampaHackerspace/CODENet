module unroundedCube(width,height,depth,radius)
{
    echo("unroundedCube: ",width,",",height,",",depth,",",radius);
    
    translate([-width/2.0,-height/2.0,0]) cube([width,height,depth]);
}

module roundedCube(width,height,depth,radius)
{
    echo("roundedCube: ",width,",",height,",",depth,",",radius);
    radius = 10.001;
    width = width - radius*2;
    height= height - radius*2;
    depth = depth;
    
    hull() 
    {
        
        // bottom plane 
        translate([-width/2,-height/2,radius])
        sphere(r=radius);    
        translate([width/2,-height/2,radius])
        sphere(r=radius);
        translate([-width/2,height/2,radius])
        sphere(r=radius);
        translate([width/2,height/2,radius])
        sphere(r=radius);
        
       
        // upper plane
        translate([-width/2,-height/2,depth-radius])
        sphere(r=radius);
        translate([width/2,-height/2,depth-radius])
        sphere(r=radius);
        translate([-width/2,height/2,depth-radius])
        sphere(r=radius);
        translate([width/2,height/2,depth-radius])
        sphere(r=radius);

    }
}

/*
Nexus 6: 10.5mm deep, 83.25 x 159.5 mm 
about a 9mm radius
camera is 23mm from top of phone, about 18mm diameter

sdr is 13mm thick, 27.1 x 85 long without a cable
about 130mm with the bent USB OTG cable

right angle OTG cable adds about 20mm at bottom of phone

*/
// Gonna go a little loose and fix it later
phoneX = 83.85; phoneY = 160.2; phoneZ = 10.5;
phoneRadius = 10;
usbY = 20;
sdrX = 27.1; sdrY = 85; sdrZ = 13;
sdrUSBY = 45;

// Start with a case maybe 3mm thicker than phone and floor 2mm
caseWall = 4; caseFloor = 2;

difference()
{
    // Bottom of the case
    translate([0,0,0]) roundedCube(phoneX+caseWall*2,phoneY+caseWall*2+usbY,phoneZ+sdrZ+caseFloor,phoneRadius);

    //%unroundedCube(phoneX+caseWall*2,phoneY+caseWall*2,phoneZ+sdrZ+caseFloor,phoneRadius+1.5);
 
    // chop it off flat across
    translate([-200,-200,phoneZ+sdrZ+caseFloor-3]) cube([400,400,20]);
    // Dig the cutout for the phone
    //translate([0,0,caseFloor+sdrZ]) roundedCube(phoneX,phoneY,phoneZ*2);
    // Dig out space for the SDR
//    translate([0,0,caseFloor]) unroundedCube(sdrX,sdrY,sdrZ*2);
    translate([0,usbY/2,caseFloor]) roundedCube(phoneX,phoneY,phoneZ*4,phoneRadius);

//    Notch out space for the usb OTG cable

   translate([8,-phoneY/2+1,5]) unroundedCube(44,20,14,0);

// a little support
    

}
//   #color("red") translate([-phoneX/2.0,-phoneY/2.0,caseFloor+sdrZ]) cube([phoneX,phoneY,phoneZ*2]);

   translate([8,-phoneY/2+1,5]) unroundedCube(0.3,20,14,0);
   translate([16,-phoneY/2+1,5]) unroundedCube(0.3,20,14,0);
   translate([24,-phoneY/2+1,5]) unroundedCube(0.3,20,14,0);
   translate([0,-phoneY/2+1,5]) unroundedCube(0.3,20,14,0);
   translate([-8,-phoneY/2+1,5]) unroundedCube(0.3,20,14,0);


