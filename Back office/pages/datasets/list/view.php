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
    + <a href="<?= Url::toDatasetsCreate(); ?>">Ajouter un jeu de donn&eacute;es</a> +
    <br /><br />
    <table border="1" style="width: 100%; border: 1px solid;">
        <thead>
            <tr>
                <td>Image</td>
                <td>Nom</td>
                <td>Voir les points</td>
                <td>Status</td>
                <td>Exporter</td>
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
                    <td><a href="<?= Url::toPoints($set->getID()); ?>">Voir</a></td>
                    <td><?= $set->getStatusStr(); ?></td>
                    <td>
                        <a href="<?= Url::toExport('json', $set->getID()); ?>" target="_blank">JSON</a> 
                        <a href="<?= Url::toExport('xml', $set->getID()); ?>" target="_blank">XML</a> 
                        <a href="<?= Url::toExport('csv', $set->getID()); ?>" target="_blank">CSV</a>
                    </td>
                </tr>
            <?php } ?>
        </tbody>
    </table></div>
<br />