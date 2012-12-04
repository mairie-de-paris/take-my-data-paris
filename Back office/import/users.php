<?php

/**
 * Copyright 2012-2013 Mairie de Paris
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

/**
 * THIS FILE MUST BE EXECUTED ONCE AND THEN REMOVED FROM THE SERVER
**/

define('EXEC', 0xCAFEBABE);

require_once(dirname(__FILE__) . '/../config.php');

echo "==========================\n";
echo "======== Migration =======\n";
echo "==========================\n\n";

echo __FILE__ . "\n";

echo 'Starts at ' . date('d/m/Y H:i:s.u') . "\n\n==========================\n";
echo "======== Structure =======\n";
echo "==========================\n\n";

echo 'Droping existing table' . "\n";
DBO::query('DROP TABLE IF EXISTS `users`;');

$query = 'CREATE TABLE `users` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `pseudo` VARCHAR(32) NOT NULL,
    `passwd` VARCHAR(42) NOT NULL,
    `email` VARCHAR(64) NOT NULL,
    `access` TINYINT NOT NULL,
    `perms` SMALLINT NOT NULL,
    `total` INT(11) NOT NULL,
    `points` TEXT NOT NULL,
    `challengs` TEXT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT="Liste des utilisateurs" AUTO_INCREMENT=1;';

echo 'Processing : ' . "\n" . $query . "\n\n";
echo 'DBO returns ' . DBO::query($query) . "\n\n==========================\n";
echo "========  Content  =======\n";
echo "==========================\n\n";

User::create(array(
    'Upseudo' => 'Guest',
    'Upasswd' => 'guest',
    'Umail' => '',
    'Uaccess' => GUEST,
    'Uperms' => NO_RIGHT));

User::create(array(
    'Upseudo' => 'Admin',
    'Upasswd' => 'admin',
    'Umail' => 'admin@tmd.fr',
    'Uaccess' => ADMIN,
    'Uperms' => FULL_RIGHT));

echo 'Ends at ' . date('d/m/Y H:i:s.u') . "\n";
