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

defined('EXEC') or die('Access forbidden');

?>
<div class="center">
    <form method="post" action="<?= Url::toHomePage(); ?>">
        <table border="1" style="width: 100%; border: 1px solid;">
            <thead>
                <tr>
                    <td>Image</td>
                    <td>Nom</td>
                    <td>Selectionner</td>
                </tr>
            </thead>
            <tbody>
                <?php
                $sets = $this->model->getDatasets();
                foreach ($sets as $set) {
                    ?>
                    <tr>
                        <td><img src="<?= CONTENT_ROOT; ?><?= $set->getID(); ?>.png" /></td>
                        <td><a href="<?= Url::toEditParsor($set->getID()); ?>"><?= $set->getName(); ?></a></td>
                        <td><input type="radio" name="homedataset" value="<?= $set->getID(); ?>" <?php if ($set->getHome() === 1) { ?> checked="checked" <?php } ?> /></td>
                    </tr>
                <?php } ?>
            </tbody>
        </table>
        <br />
        <input type="submit" name="choose" value="Valider" />
    </form>
</div>