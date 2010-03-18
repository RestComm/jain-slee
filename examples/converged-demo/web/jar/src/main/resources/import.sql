delete from ORDERLINES
delete from ORDERS
delete from USERS
delete from INVENTORY
delete from PRODUCT_CATEGORY
delete from PRODUCT_ACTORS
delete from ACTORS
delete from PRODUCTS
delete from CATEGORIES

INSERT INTO USERS (USERID,DTYPE,USERNAME,PASSWORD,FIRSTNAME,LASTNAME) VALUES (1,'admin','manager','password','Albus', 'Dumblebore')
INSERT INTO USERS (USERID,DTYPE,FIRSTNAME,LASTNAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,EMAIL,PHONE,CREDITCARDTYPE,CC_NUM,CC_MONTH,CC_YEAR,USERNAME,PASSWORD) VALUES (2,'customer','Harry','Potter','4 Privet Drive','Cupboard under the Stairs','QSDPAGD','SD',24101,'h.potter@hogwarts.edu','sip:abhayani@192.168.0.100:5059',1,'1979279217775911',03,2012,'user1','password')
INSERT INTO USERS (USERID,DTYPE,FIRSTNAME,LASTNAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,EMAIL,PHONE,CREDITCARDTYPE,CC_NUM,CC_MONTH,CC_YEAR,USERNAME,PASSWORD) VALUES (3,'customer','Hermione','Granger','5119315633 Dell Way','','YNCERXJ','AZ',11802,'h.granger@hogwarts.edu','sip:abhayani@127.0.0.1:5059',1,'3144519586581737',11,2012,'user2','password')
INSERT INTO USERS (USERID,DTYPE,FIRSTNAME,LASTNAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,EMAIL,PHONE,CREDITCARDTYPE,CC_NUM,CC_MONTH,CC_YEAR,USERNAME,PASSWORD) VALUES (4,'customer','Ron','Weasley','6297761196 Dell Way','','LWVIFXJ','OH',96082,'r.weasley@hogwarts.edu','sip:+919960639901@callwithus.com',4,'8728086929768325',12,2010,'user3','password')
INSERT INTO USERS (USERID,DTYPE,FIRSTNAME,LASTNAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,EMAIL,PHONE,CREDITCARDTYPE,CC_NUM,CC_MONTH,CC_YEAR,USERNAME,PASSWORD) VALUES (5,'customer','Sanjeewa','Vijayratna','9862764981 Dell Way','','HOKEXCD','MS',78442,'n.longbottom@hogwarts.edu','sip:+94773123543@callwithus.com',5,'7160005148965866',09,2009,'user4','password')
INSERT INTO USERS (USERID,DTYPE,FIRSTNAME,LASTNAME,ADDRESS1,ADDRESS2,CITY,STATE,ZIP,EMAIL,PHONE,CREDITCARDTYPE,CC_NUM,CC_MONTH,CC_YEAR,USERNAME,PASSWORD) VALUES (6,'customer','Ginny','Weasley','2841895775 Dell Way','','RZQTCDN','AZ',16291,'g.weasley@hogwarts.edu','2841895775',3,'8377095518168063',10,2010,'user5','password')

insert into ACTORS (ID, NAME) values (1, 'Tom Hanks')
insert into ACTORS (ID, NAME) values (2, 'Katie Holmes')
insert into ACTORS (ID, NAME) values (3, 'Drew Barrymore')
insert into ACTORS (ID, NAME) values (4, 'Daniel Radcliffe')
insert into ACTORS (ID, NAME) values (5, 'Jim Carrey')
insert into ACTORS (ID, NAME) values (6, 'Scarlett Johansson')
insert into ACTORS (ID, NAME) values (7, 'Bill Murray')
insert into ACTORS (ID, NAME) values (8, 'Owen Wilson')
insert into ACTORS (ID, NAME) values (9, 'Luke Wilson')
insert into ACTORS (ID, NAME) values (10, 'Tobey Maguire')
insert into ACTORS (ID, NAME) values (11, 'John Cusak')
insert into ACTORS (ID, NAME) values (12, 'Jack Black')
insert into ACTORS (ID, NAME) values (13, 'Keanu Reeves')
insert into ACTORS (ID, NAME) values (14, 'Christopher Reeve')
insert into ACTORS (ID, NAME) values (15, 'Harrison Ford')
insert into ACTORS (ID, NAME) values (16, 'Kirsten Dunst')
insert into ACTORS (ID, NAME) values (17, 'Elijah Wood')
insert into ACTORS (ID, NAME) values (18, 'Laurence Fishburne')
insert into ACTORS (ID, NAME) values (19, 'Meg Ryan')
insert into ACTORS (ID, NAME) values (20, 'Billy Crystal')
insert into ACTORS (ID, NAME) values (21, 'Wesley Snipes')
insert into ACTORS (ID, NAME) values (22, 'Ewan McGregor')
insert into ACTORS (ID, NAME) values (23, 'Natalie Portman')
insert into ACTORS (ID, NAME) values (24, 'Jon Heder')
insert into ACTORS (ID, NAME) values (25, 'Vince Vaughn')
insert into ACTORS (ID, NAME) values (26, 'Ben Stiller')
insert into ACTORS (ID, NAME) values (27, 'Matt Damon')
insert into ACTORS (ID, NAME) values (28, 'Jodie Foster')
insert into ACTORS (ID, NAME) values (29, 'Matthew McConaughey')
insert into ACTORS (ID, NAME) values (30, 'Ed Harris')
insert into ACTORS (ID, NAME) values (31, 'Ralph Fiennes')
insert into ACTORS (ID, NAME) values (32, 'Gwyneth Paltrow')
insert into ACTORS (ID, NAME) values (33, 'Brad Pitt')
insert into ACTORS (ID, NAME) values (34, 'Angelina Jolie')
insert into ACTORS (ID, NAME) values (35, 'Edward Norton')
insert into ACTORS (ID, NAME) values (36, 'Adam Sandler')
insert into ACTORS (ID, NAME) values (37, 'Johnny Depp')
insert into ACTORS (ID, NAME) values (38, 'Keira Knightley')
insert into ACTORS (ID, NAME) values (39, 'Robin Williams')
insert into ACTORS (ID, NAME) values (40, 'Tom Cruise')
insert into ACTORS (ID, NAME) values (41, 'Bruce Willis')
insert into ACTORS (ID, NAME) values (42, 'Patrick Stewart')
insert into ACTORS (ID, NAME) values (43, 'Halle Berry')
insert into ACTORS (ID, NAME) values (44, 'Jennifer Aniston')
insert into ACTORS (ID, NAME) values (45, 'Julia Stiles')
insert into ACTORS (ID, NAME) values (46, 'Winona Ryder')
insert into ACTORS (ID, NAME) values (47, 'Kate Hudson')
insert into ACTORS (ID, NAME) values (48, 'Uma Thurman')
insert into ACTORS (ID, NAME) values (49, 'Julia Roberts')
insert into ACTORS (ID, NAME) values (50, 'Steve Carell')
insert into ACTORS (ID, NAME) values (51, 'Catherine Keener')
insert into ACTORS (ID, NAME) values (52, 'Franka Potente')
insert into ACTORS (ID, NAME) values (53, 'Catherine Zeta-Jones')
insert into ACTORS (ID, NAME) values (54, 'Tim Robbins')
insert into ACTORS (ID, NAME) values (55, 'Cate Blanchett')
insert into ACTORS (ID, NAME) values (56, 'Orlando Bloom')
insert into ACTORS (ID, NAME) values (57, 'Liv Tyler')
insert into ACTORS (ID, NAME) values (58, 'Ben Affleck')
insert into ACTORS (ID, NAME) values (59, 'Jack Nicholson')
insert into ACTORS (ID, NAME) values (60, 'Meryl Streep')
insert into ACTORS (ID, NAME) values (61, 'John Travolta')
insert into ACTORS (ID, NAME) values (62, 'Cary Grant')
insert into ACTORS (ID, NAME) values (63, 'Woody Allen')
insert into ACTORS (ID, NAME) values (64, 'Will Smith')
insert into ACTORS (ID, NAME) values (65, 'Sean Connery')
insert into ACTORS (ID, NAME) values (66, 'Kevin Costner')
insert into ACTORS (ID, NAME) values (67, 'Arnold Schwarzenegger')
insert into ACTORS (ID, NAME) values (68, 'Audrey Hepburn')
insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('1', '11fj-v0siZL._AA160_', 'South Shore Furniture Step One Collection Full/Queen Platform Bed', 199.00, 'http://ecx.images-amazon.com/images/I/41WPZpd93WL._SL500_AA300_.jpg', 'Queen sized bed. Contemporary, low platform bed frame for full or queen mattress; 2 roomy storage drawers built into platform. Environmentally Preferred Product (EPP) certified construction of engineered recycled wood and fibers with black finish; metal handles. Metal slides, with dampers and stops, ensure smooth, safe opening and closing of drawers. Adult assembly required; in addition the separate purchase of mattress, assorted matching bedroom pieces, including a headboard, are available. Bed frame measures 80-1/2 inches long by 61-4/5 inches wide by 9 inches high and weighs 136 pounds; 5-year manufacturer warranty.');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (1, 68)
insert into CATEGORIES (CATEGORY, NAME) values (1, 'Living Room');
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (1, 1);
insert into CATEGORIES (CATEGORY, NAME) values (2, 'Bedroom');
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (1, 2);
insert into CATEGORIES (CATEGORY, NAME) values (3, 'Home Décor');
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (1, 3);
insert into CATEGORIES (CATEGORY, NAME) values (4, 'Home Office');
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (1, 4);
insert into CATEGORIES (CATEGORY, NAME) values (5, 'Lighting');
insert into CATEGORIES (CATEGORY, NAME) values (6, 'Rugs');
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (1, 1, 84, 33);

insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('2', '212Qh3dT1mL._AA160_', 'Techcraft Veneto Series ABS60 TV Stand', 164.98, 'http://ecx.images-amazon.com/images/I/31g2pN8%2B39L._SL500_AA300_.jpg', 'Well desgined and modern shaped cabinet. Framed doors to conceal components. Supports up to 200 lbs. Molded top and shaped skirt gives it an elegant appearance. Works with PLM50 floating mount. Dimensions: 24" H x 60" W x 23.625" D.');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (2, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (2, 1);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (2, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (2, 3);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (2, 4);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (2, 2, 77,32);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('3', '21Upbbe5v6L._AA160_', 'Prepac 60-Inch Plasma TV Console with Media Storage', 273.71, 'http://ecx.images-amazon.com/images/I/41ElUAgInKL._SL500_AA300_.jpg', 'Sleek TV console for 60-inch flat-panel plasma screens; with extensive storage space for DVDs, CDs, VHS, and other media. Ready-to-assemble console constructed with composite MDF and Melamine laminate; black finish. Adjustable component, cabinet, and door shelves for custom-sized storage compartments; sliding back access door and cable management holes. With brushed metal cabinet handles and dowel legs; 150-pound weight capacity. Measures 58 by 19-3/4 by 26-3/4 inches upon assembly and weighs 125 pounds; limited 5-year warranty.  ');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (3, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (3, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (3, 3);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (3, 4);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (3, 3, 79, 31);

insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('4', '21EZ34KY9EL._AA160_', 'Stork Craft Hoop Glider and Ottoman', 155.00, 'http://ecx.images-amazon.com/images/I/41zrYiG4grL._AA300_.jpg', 'Generous seating room with padded arms. Arm cushions have a pocket for paper, magazines, and TV remote. Back and seat cushions are hand washable only. Ideal for any room in your house. Matching ottoman included. ');

insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (4, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (4, 1);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (4, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (4, 5);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (4, 4, 28, 30);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('5', '11PgsxMDwRL._AA160_', 'Bush Furniture Midnight Mist Audio Tower', 238.13, 'http://ecx.images-amazon.com/images/I/41baomZR5WL._SL500_AA300_.jpg', 'Modern audio tower offers bold style with sophisticated double-frame design. Textured black and chrome finishes on double frame; perforated back spine for easy wire management. 5 black, tempered-glass shelves rest on vibration-dampening pads for enhanced sound quality. Assembly required; tested for tip-free safety. Measures 25-5/8 inches wide by 22-3/8 inches deep by 41-3/4 inches high; 6-year warranty. ');

insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (5, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (5, 2);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (5, 5, 28, 29);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('6', '21GvIl4-yhL._AA160_', 'Ergonomic Office Chair - High Back Contemporary Executive Swivel Chair', 79.98, 'http://ecx.images-amazon.com/images/I/41zk3Ko4RXL._SL500_AA300_.jpg', 'Black Vinyl Upholstery. Seat with Passive Lumbar Support. Pneumatic Seat Height Adjustment. Black Nylon Loop Arms. Heavy-Duty Nylon Base.');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (6, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (6, 2);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (6, 6, 73, 28);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('7', '21zCwmFUN1L._AA160_', '3-Seater Hammock Canopy Swing ', 274.00, 'http://ecx.images-amazon.com/images/I/515dpKgfq1L._SL500_AA300_.jpg', 'Dimensions: 76L x 48D x 67H inches. Canopy: 77 L x 45 D". Folds Flat to make a day bed and locks in place. Canopy can be tilted to any angle. Includes Cushions and 2 pillows - FREE. ');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (7, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (7, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (7, 4);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (7, 5);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (7, 6);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (7, 7, 95, 28);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('8', '11Yw-aDb-EL._AA160_', 'Portable Folding Hammock with Carry Bag', 79.00, 'http://ecx.images-amazon.com/images/I/41y36rSg50L._SL500_AA300_.jpg', 'An easy-up, portable hammock lets you take comfort on the go. Durable powder-coated steel frame; Textilene fabric; chain suspension. Includes a removable pillow for head rest or lumbar support. Packs up easily and stores in its own included carrying case with handle. Measures 93 by 40 by 26 inches.');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (8, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (8, 2);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (8, 8, 65, 1);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('9', '21WQCZGMEJL._AA160_', 'Cat Furniture Tree Condo Toy House Scratcher Post Pet House F18', 92.00, 'http://ecx.images-amazon.com/images/I/21ZwkzUybLL._SL160_AA160_.jpg', 'Model No: F18 (Front) Bottom post can turn. Color : Grey & Tam. Overall Size : 26"W x 20"L x 62"H. Hanging Balls and Rope included. Covering Material : Only the posts is carpet, rest are faux fur.');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (9, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (9, 2);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (9, 9, 24, 11);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('10', '11RyfyWB6mL._AA160_', 'Babi Italia Scandi Convertible Drop Side Crib, White', 104.99, 'http://ecx.images-amazon.com/images/I/41%2BCwNhw4xL._SL500_AA300_.jpg', 'Easily converts from a crib to a toddler bed and then day bed for years of service. Drop side crib offers no exposed hardware. Useful sliding drawer for additional storage. JPMA Certified and comes with 5 year manufacturers warranty. Finishes certified that they are free of lead and heavy metals. ');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (10, 68)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (10, 1);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (10, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (10, 3);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (10, 4);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (10, 10, 26, 6);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('11', '21Ulo1NNKyL._AA160_', 'Versailles Collection - Brown 9 Piece Patio Furniture Outdoor Wicker Garden Sectional', 1799.98, 'http://ecx.images-amazon.com/images/I/51FR%2BF8Q3qL._SL500_AA300_.jpg', 'Synthetic resin wicker can be used outdoors and is completly weather proof. Contract grade aluminum frame construction. High quality All weather Fabric creates incredibly comfortable cushions. Brown Multicolored Weaved Wicker/Rattan and tan cushions. Custom colors available for cushions on this page. Stainless steel hardware and Nylon Glides will not mar hard floor surfaces. Big extra wide seating area with cushions that are weather resistent and washable.');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (11, 1)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (11, 1);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (11, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (11, 3);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (11, 4);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (11, 11, 49, 8);


insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('12', '11cQNX9qArL._AA160_', 'Malibu Collection - Espresso Brown Patio Furniture Outdoor Wicker Sectional', 1999.99, 'http://ecx.images-amazon.com/images/I/51Pe3LnlX0L._SL500_AA300_.jpg', 'Synthetic resin wicker can be used outdoors and is completly weather proof. Contract grade aluminum frame construction High quality all weather Fabric creates incredibly comfortable cushions, covers are removable and washable via zippers. Non-mar nylon glides will not mar floor surfaces Espresso Brown Multicolor Weave with light camel colored cushions as shown in photo. No assembly required');

insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (12, 1)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (12, 1);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (12, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (12, 3);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (12, 12, 40, 8);

insert into PRODUCTS (PROD_ID, ASIN, TITLE, PRICE, IMAGE_URL, DESCRIPTION) values ('13', '21KOXaXEuzL._AA160_', 'Strathwood Camano All-Weather Wicker Side Table', 99.99, 'http://ecx.images-amazon.com/images/I/51HiMGVpnLL._SL500_AA300_.jpg', 'Handsome, durable side synthetic wicker table for outdoor use. Crafted from weather-defying wicker fiber to withstand fading, weathering, and aging. Quality-constructed with durable, weatherproof aluminum frame underneath. Plastic castors on leg bottoms to protect wooden floors. Easy to clean; measures 19 by 19 by 13 inches (LxWxH).');
insert into PRODUCT_ACTORS (PROD_ID,ACTOR_ID) values (13, 1)
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (13, 1);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (13, 2);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (13, 3);
insert into PRODUCT_CATEGORY (PROD_ID, CATEGORY) values (13, 4);
insert into INVENTORY (INV_ID, PROD_ID, QUAN_IN_STOCK, SALES) values (13, 13, 87, 2);
