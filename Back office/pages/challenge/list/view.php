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
<div class="center">
    + <a href="<?= Url::toUserCreate(); ?>">Creer un nouvel utilisateur</a> +
    <br /><br />
    <?php $list = $this->model->getUsers(); ?>
    <table border="1" style="width: 100%; border: 1px solid;">
        <thead>
            <tr>
                <td>#ID</td>
                <td>Nom d'utilisateur</td>
                <td>Droits</td>
                <td>Editer le mot de passe</td>
                <td>Supprimer l'utilisateur</td>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($list as $user) { ?>
                <tr>
                    <td><?= $user->getID(); ?></td>
                    <td><?= $user->getUname(); ?></td>
                    <td>
                        <?php
                        $has = false;
                        if ($user->can(READ_RIGHT)) {
                            echo 'Lecture ';
                            $has = true;
                        }
                        if ($user->can(WRITE_RIGHT)) {
                            echo 'Ecriture ';
                            $has = true;
                        }
                        if ($user->can(DEL_RIGHT)) {
                            echo 'Suppression';
                            $has = true;
                        }
                        if (!$has) {
                            echo 'Aucun droit';
                        }
                        ?>
                    </td>
                    <td><a href="<?= Url::toEditPasswd($user->getID()); ?>">Editer</a></td>
                    <td><a href="<?= Url::toDelUser($user->getID()); ?>">Supprimer</a></td>
                </tr>
            <?php } ?>
        </tbody>
    </table>
</div>