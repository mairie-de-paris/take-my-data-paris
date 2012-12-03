<?php

/**
 * Copyright 2012-2013 The Apache Software Foundation
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

defined('EXEC') or die('Access forbidden');

?>
<!DOCTYPE html>
<html>
    <head>
        <title>TakeMyData</title>
        <meta http-equiv="content-type" content="text/html; charset=latin1" />
        <meta http-equiv="content-style-type" content="text/css" />
        <link rel="stylesheet" type="text/css" href="<?= RESSOURCES_ROOT; ?>/css/styles.css" />
        <link rel="stylesheet" type="text/css" href="<?= RESSOURCES_ROOT; ?>/css/jquery-ui.css" />
        <script type="text/javascript" src="<?= RESSOURCES_ROOT; ?>/res/jquery.js"></script>
        <script type="text/javascript" src="<?= RESSOURCES_ROOT; ?>/res/jquery-ui.js"></script>
        <script type="text/javascript">
<?php $this->showJS(); ?>
        </script> 
    </head>

    <body>
        <div id="page">
            <?php $this->showPage(); ?>
        </div>
    </body>
</html>