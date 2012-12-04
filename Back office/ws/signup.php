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
 *limitations under the License.
**/

define('EXEC', 0xA51D1E);

require_once('../config.php');

if (!empty($_POST['mail']) && !empty($_POST['name']) && !empty($_POST['pwd'])) {
    if (!filter_var($_POST['mail'], FILTER_VALIDATE_EMAIL)) {
        Exec::e('bad_mail');
    } elseif (User::emailExist($_POST['mail'])) {
        $user = User::connect($_POST['name'], $_POST['pwd']);

        if ($user) {
            Exec::e(json_encode($user->getObject()));
        } else {
            Exec::e('bad_pwd');
        }
    } else {
        $data = array(
            'Upseudo' => $_POST['name'],
            'Upasswd' => $_POST['pwd'],
            'Uaccess' => GUEST,
            'Umail' => $_POST['mail'],
            'Uperms' => NO_RIGHT
        );
        $id = User::create($data);
        $user = User::getFromID($id);
        Exec::e(json_encode($user->getObject()));
    }
} else {
    Exec::e('bad_param');
}