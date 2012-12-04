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
    <?php
    $columns = $this->current->getColumns();
    $rules = $this->current->getRules()
    ?>
    <form action="<?= Url::toDatasetImport($this->parsor); ?>" method="post">
        <table style="margin:auto;">
            <thead>
                <tr>
                    <td>Champ</td>
                    <td>Colonne</td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Latitude</td>
                    <td>
                        <select name="lat">
                            <option value="" selected="selected"></option>
                            <?php foreach ($columns as $col) { ?>
                                <option value="<?= $col; ?>"><?= $col; ?></option>
                            <?php } ?>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Longitude</td>
                    <td>
                        <select name="lon">
                            <option value="" selected="selected"></option>
                            <?php foreach ($columns as $col) { ?>
                                <option value="<?= $col; ?>"><?= $col; ?></option>
                            <?php } ?>
                        </select>
                    </td>
                </tr>
                <?php foreach ($rules as $rule) { ?>
                    <tr>
                        <td><?= $rule->getName(); ?></td>
                        <td>
                            <select name="rule[<?= $rule->getID() ?>]">
                                <option value="" selected="selected"></option>
                                <?php foreach ($columns as $col) { ?>
                                    <option value="<?= $col; ?>"><?= $col; ?></option>
                                <?php } ?>
                            </select>
                        </td>
                    </tr>
                <?php } ?>
            </tbody>
        </table>
        <br />
        <input type="hidden" name="id" value="<?= $this->parsor; ?>" />
        <input type="hidden" name="source" value="<?= urlencode($this->source); ?>" />
        <input type="submit" name="submit" value="Importer" />
    </form>
</div>